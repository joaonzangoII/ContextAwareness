package app.androidgeofence;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.androidgeofence.json.SafeZoneJson;
import app.androidgeofence.model.SafeZone;
import app.androidgeofence.model.User;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MapActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        LocationListener, ResultCallback<Status>,
        RealmChangeListener<RealmResults<SafeZone>> {
    private static final String TAG = MapActivity.class.getSimpleName();
    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Safe Zone";
    private static final int REQ_PERMISSION = 1;
    private final int UPDATE_INTERVAL = 3 * 60 * 1000; // 3 minutes
    private final int FASTEST_INTERVAL = 30 * 1000;  // 30 secs
    private final int GEOFENCE_REQ_CODE = 0;
    private TextView textLat, textLong;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private Marker locationMarker;
    private List<Marker> safeZonesMarkerList = new ArrayList<>();
    private LocationRequest locationRequest;
    private PendingIntent geoFencePendingIntent;
    private SessionManager session;
    private Realm realm;
    private RealmResults<SafeZone> safeZonesList;
    // Draw Geofence circle on GoogleMap
    private Circle geoFenceLimits;
    private boolean isInGeofence;
    private ProgressDialog pDialog;
    private User user;

    // Initialize GoogleMaps
    public static Intent makeNotificationIntent(final Context applicationContext,
                                                final String msg) {
        return new Intent(App.getContext(), MapActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        if (!session.isLoggedIn()) {
            // User is already logged in. Take him to map activity
            Intent intent = new Intent(MapActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance();
        // session manager
        session = new SessionManager(getApplicationContext());
        // Check if user is already logged in or not
        if (!session.isLoggedIn()) {
            // User is already logged in. Take him to map activity
            Intent intent = new Intent(MapActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        user = User.query(realm).byId((long) session.getLoggedInUserId());
        if (user == null) {
            final LoginActivity login = new LoginActivity();
            login.logoutUser(MapActivity.this, session);
        }

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        setLoading(true);

        // realm.delete(GeofenceCoordinate.class);
        fetchGeofencesAsync();
        // Defined in mili seconds.
        safeZonesList = SafeZone.query(realm).all().findAll();
        safeZonesList.addChangeListener(this);

        if (!session.isLoggedIn()) {
            final LoginActivity login = new LoginActivity();
            login.logoutUser(MapActivity.this, session);
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Geofences");
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        textLat = (TextView) findViewById(R.id.lat);
        textLong = (TextView) findViewById(R.id.lon);

        // initialize GoogleMaps
        initGMaps();
        // create GoogleApiClient
        createGoogleApi();
    }

    private void initGMaps() {
        mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Callback called when Map is ready
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "onMapReady()");
        map = googleMap;

        for (final SafeZone geo : safeZonesList) {
            final String title = geo.latitude + ", " + geo.longitude;
            final Marker marker = map.addMarker(new MarkerOptions()
                    .position(geo.getLatLng())
                    .title(title)
                    .snippet(geo.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            markerForGeofence(geo.getLatLng());
        }

        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
    }

    // Callback called when Marker is touched
    @Override
    public boolean onMarkerClick(final Marker marker) {
        Log.e(TAG, "onMarkerClickListener: " + marker.getPosition());
        for (final SafeZone geo : safeZonesList) {
            if (marker.getPosition().equals(new LatLng(geo.latitude, geo.longitude))) {
                final Location geofenceLoc = new Location("");
                geofenceLoc.setLatitude(geo.latitude);
                geofenceLoc.setLongitude(geo.longitude);
                if (geofenceLoc.distanceTo(lastLocation) < 500.00f) {
                    Toast.makeText(this, "You close to a geofence by : "
                            + geofenceLoc.distanceTo(lastLocation)
                            + " in meters", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.e(TAG, "onMapClick(" + latLng + ")");
    }

    // Create GoogleApiClient instance
    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Disconnect GoogleApiClient when stopping Activity
        googleApiClient.disconnect();
    }

    // GoogleApiClient.ConnectionCallbacks suspended
    @Override
    public void onConnectionSuspended(final int i) {
        Log.w(TAG, "onConnectionSuspended()");
    }

    // GoogleApiClient.OnConnectionFailedListener fail
    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");
    }

    // GoogleApiClient.ConnectionCallbacks connected
    @Override
    public void onConnected(@Nullable final Bundle bundle) {
        Log.i(TAG, "onConnected()");
        getLastKnownLocation();
    }

    // Get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if (checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                writeLastLocation();
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        } else askPermission();
    }

    // Start location Updates
    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (checkPermission()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onLocationChanged(final Location location) {
        Log.d(TAG, "onLocationChanged [" + location + "]");
        lastLocation = location;
        writeActualLocation(location);
        startGeofence();
        isInGeofence = false;
        if (safeZonesList.size() > 0) {
            SafeZone geoIn = safeZonesList.get(0);
            for (final SafeZone safeZone : safeZonesList) {
                final Location geofenceLoc = new Location("");
                geofenceLoc.setLatitude(safeZone.latitude);
                geofenceLoc.setLongitude(safeZone.longitude);

                if (geofenceLoc.distanceTo(lastLocation) < safeZone.radius) {
                    isInGeofence = true;
                    geoIn = safeZone;
                }
            }

            //        final String title,
            //        final String body,
            //        final String user_id,

            if (user.getUserType().equals("normal")) {
                if (!isInGeofence) {
                    addAnEventZone("Outside a safe zone", "Currently on an unsafe zone", String.valueOf(user.getId()), String.valueOf("1"));
                    Toast.makeText(MapActivity.this, "You are out of a safe zone", Toast.LENGTH_SHORT).show();
                } else {
                    addAnEventZone("Enterered a safe zone", "Currently on a safe zone", String.valueOf(user.getId()), String.valueOf(geoIn.getId()));
                    Toast.makeText(MapActivity.this, "You are inside a safe zone", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    getLastKnownLocation();

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
    }

    // Write location coordinates on UI
    private void writeActualLocation(final Location location) {
        textLat.setText(String.format("Lat: %s", location.getLatitude()));
        textLong.setText(String.format("Long: %s", location.getLongitude()));
        markerLocation(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    // Create a Location Marker
    private void markerLocation(final LatLng latLng) {
        Log.i(TAG, "markerLocation(" + latLng + ")");
        final String title = latLng.latitude + ", " + latLng.longitude;
        if (map != null) {
            // Remove the anterior marker
            if (locationMarker != null) {
                locationMarker.remove();
            }

            locationMarker = map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .snippet("My Current Location")
                    .title(title));
            //markerForGeofence(latLng);

            final float zoom = 14f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            map.animateCamera(cameraUpdate);
        }
    }

    // Create a marker for the geofence creation
    private void markerForGeofence(final LatLng latLng) {
        Log.i(TAG, "markerForGeofence(" + latLng + ")");
        final String title = latLng.latitude + ", " + latLng.longitude;
        // Define marker options
        final MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);
        if (map != null) {
            // Remove last safeZonesMarkerList
            if (safeZonesMarkerList != null) {
                //safeZonesMarkerList.remove();
            }

            safeZonesMarkerList.add(map.addMarker(markerOptions));
        }
    }

    // Create a Geofence
    private Geofence createGeofence(final LatLng latLng,
                                    final float radius) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(
                        Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest(final Geofence geofence) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }

    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if (geoFencePendingIntent != null)
            return geoFencePendingIntent;

        final Intent intent = new Intent(this, GeofenceTrasitionService.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(final GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission())
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback(this);
    }

    @Override
    public void onResult(@NonNull final Status status) {
        Log.i(TAG, "onResult: " + status);
        if (status.isSuccess()) {
            drawGeofence();
        } else {
            // inform about fail
        }
    }

    private void drawGeofence() {
        Log.d(TAG, "drawGeofence()");
        if (geoFenceLimits != null)
            geoFenceLimits.remove();

        for (final Marker geo : safeZonesMarkerList) {
            float radius = 500.0f;
            for (final SafeZone safeZone : safeZonesList) {
                if (safeZone.getLatLng().equals(geo.getPosition())) {
                    radius = safeZone.radius;
                    break;
                }
            }

            final CircleOptions circleOptions = new CircleOptions()
                    .center(geo.getPosition())
                    .strokeColor(Color.argb(50, 70, 70, 70))
                    .fillColor(Color.argb(100, 150, 150, 150))
                    .radius(radius);
            geoFenceLimits = map.addCircle(circleOptions);
        }
    }

    // Start Geofence creation process
    private void startGeofence() {
        Log.i(TAG, "startGeofence()");
        for (final Marker geo : safeZonesMarkerList) {
            if (geo != null) {
                float radius = 500.0f;
                for (final SafeZone safeZone : safeZonesList) {
                    if (safeZone.getLatLng() == geo.getPosition()) {
                        radius = safeZone.radius;
                    }
                }

                final Geofence geofence = createGeofence(geo.getPosition(), radius);
                final GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
                addGeofence(geofenceRequest);
            } else {
                Log.e(TAG, "Geofence marker is null");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            case R.id.action_refresh: {
                setLoading(true);
                fetchGeofencesAsync();
                return true;
            }
            case R.id.action_profile: {
                final Intent intent = new Intent(MapActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_logout: {
                final LoginActivity login = new LoginActivity();
                login.logoutUser(MapActivity.this, session);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetchGeofencesAsync() {
        final String url = session.getServerUrl() + Config.URL_GET_GEOFENCES;
        pDialog.setMessage("Get All Geofences...");
        final StringRequest stringRequestq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                final Gson gson = new GsonBuilder().create();
                final Type type = new TypeToken<List<SafeZoneJson>>() {
                }.getType();

                final List<SafeZoneJson> list = gson.fromJson(response, type);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(final Realm realm) {
                        safeZonesList.deleteAllFromRealm();
                        for (final SafeZoneJson geo : list) {
                            SafeZone.createOrUpdate(realm,
                                    geo.id,
                                    geo.name,
                                    new LatLng(geo.latitude, geo.longitude),
                                    geo.radius,
                                    geo.description,
                                    new Date().toString(),
                                    null
                            );
                        }
                    }
                });
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                pDialog.dismiss();
            }
        });
        // Adding request to request queue
        App.getInstance().addToRequestQueue(stringRequestq, TAG);
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUserInGeofence() {
        Toast.makeText(this, "You will be logged out in 10 seconds because you not in a safe zone",
                Toast.LENGTH_LONG).show();
        new Handler().postDelayed((new Runnable() {
            @Override
            public void run() {
                session.setLogin(false);
                // delete all users
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(final Realm realm) {
                        realm.delete(User.class);
                    }
                });
                // Launching the login activity
                final Intent intent = new Intent(MapActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }), 10000);
    }

    @Override
    public void onChange(RealmResults<SafeZone> geofences) {
        if (geofences != null) {
            if (map != null)
                for (final SafeZone geo : safeZonesList) {
                    final String title = geo.latitude + ", " + geo.longitude;
                    final Marker marker = map.addMarker(new MarkerOptions()
                            .position(geo.getLatLng())
                            .title(title)
                            .snippet(geo.name)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                    markerForGeofence(geo.getLatLng());
                }
        }
    }

    /**
     * function to verify login details to realm
     * firstname,middlename, idNumber, lastname,email, password, passwordConfirm
     */
    private void addAnEventZone(
            final String title,
            final String body,
            final String user_id,
            final String safe_zone_id) {
        // Tag used to cancel the request
        final String tag_string_req = "req_add_event";
        pDialog.setMessage("Adding an event ...");
        setLoading(true);
        final String url = session.getServerUrl() + Config.URL_ADD_EVENT;
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Log.e(TAG, "Adding an event: " + response);
                setLoading(false);
                try {
                    final JSONObject jObj = new JSONObject(response);
                    final boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(MapActivity.this,
                                "Successfully added an event",
                                Toast.LENGTH_LONG).show();

                        final Gson gson = new GsonBuilder().create();
                        final Type type = new TypeToken<SafeZoneJson>() {
                        }.getType();
                        final JSONObject event = jObj.getJSONObject("event");
                        //                        final SafeZoneJson geo = gson.fromJson(event.toString(), type);
                        //                        realm.executeTransaction(new Realm.Transaction() {
                        //                            @Override
                        //                            public void execute(final Realm realm) {
                        //                                SafeZone.createOrUpdate(realm,
                        //                                        geo.id,
                        //                                        geo.name,
                        //                                        new LatLng(geo.latitude, geo.longitude),
                        //                                        geo.radius,
                        //                                        geo.description,
                        //                                        new Date().toString(),
                        //                                        null
                        //                                );
                        //                            }
                        //                        });
                    } else {
                        final StringBuilder stringBuilder = new StringBuilder();
                        // Error in login. Get the error message
                        final Boolean errorStatus = jObj.getBoolean("error");
                        String errorMsg = jObj.getString("code");
                        final JSONObject messages = jObj.getJSONObject("messages");
                        if (messages.has("title")) {
                            final String title = messages.getJSONArray("title").get(0).toString();
                            stringBuilder.append("\n").append(title);
                        }

                        if (messages.has("body")) {
                            final String body = messages.getJSONArray("body").get(0).toString();
                            stringBuilder.append("\n").append(body);
                        }

                        if (messages.has("user_id")) {
                            final String user_id = messages.getJSONArray("user_id").get(0).toString();
                            stringBuilder.append("\n").append(user_id);
                        }

                        if (messages.has("safe_zone_id")) {
                            final String safe_zone_id = messages.getJSONArray("safe_zone_id").get(0).toString();
                            stringBuilder.append("\n").append(safe_zone_id);
                        }
                        Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
                    }
                } catch (final JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.e(TAG, "Adding an Event Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                setLoading(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("title", title);
                params.put("body", body);
                params.put("user_id", user_id);
                params.put("safe_zone_id", safe_zone_id);
                return params;
            }
        };

        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void setLoading(final boolean isLoading) {
        if (isLoading) {
            pDialog.show();
        } else {
            pDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
