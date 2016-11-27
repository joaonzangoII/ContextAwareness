package app.androidgeofence;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.androidgeofence.json.SafeZoneJson;
import app.androidgeofence.model.SafeZone;
import io.realm.Realm;
import io.realm.RealmResults;

public class ViewAllSafeZonesActivity extends AppCompatActivity {
    private static final String TAG = ViewAllSafeZonesActivity.class.getSimpleName();
    private SessionManager session;
    private Realm realm;
    private RealmResults<SafeZone> safeZonesList;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_safe_zones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("All Safe Zones");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        realm = Realm.getDefaultInstance();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // session manager
        session = new SessionManager(getApplicationContext());

        safeZonesList = SafeZone.query(realm).all().findAllSorted("name");
        //safeZonesList.addChangeListener(this);

        RecyclerView allSafeZonesRecyclerView = (RecyclerView) findViewById(R.id.allSafeZonesRecyclerView);
        allSafeZonesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final AllSafeZonesAdapter allSafeZonesAdapter = new AllSafeZonesAdapter(this, safeZonesList);
        allSafeZonesRecyclerView.setAdapter(allSafeZonesAdapter);
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
            case R.id.action_logout: {

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setLoading(final boolean isLoading) {
        if (isLoading) {
            pDialog.show();
        } else {
            pDialog.dismiss();
        }
    }

    public LayoutInflater getInflater() {
        return (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class AllSafeZonesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;
        private List<SafeZone> safeZonesList = Collections.emptyList();

        public AllSafeZonesAdapter(final Context context,
                                   final List<SafeZone> safeZonesList) {
            this.context = context;
            this.safeZonesList = safeZonesList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                          final int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ViewHolder(inflater.inflate(R.layout.safe_zones_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder,
                                     final int position) {
            final ViewHolder holder1 = (ViewHolder) holder;
            final SafeZone safeZone = getItem(position);
            holder1.name
                    .setText(Html.fromHtml(String.format("<b>Name:</b> %s", safeZone.getName())));
            holder1.latitude
                    .setText(Html.fromHtml(String.format("<b>Latitude:</b> %s", String.valueOf(safeZone.getLatLng().latitude))));
            holder1.longitude
                    .setText(Html.fromHtml(String.format("<b>Longitude:</b> %s", String.valueOf(safeZone.getLatLng().longitude))));
            holder1.radius
                    .setText(Html.fromHtml(String.format("<b>Radius:</b> %s", String.valueOf(safeZone.getRadius()))));

            holder1.btnUpdateSafeZone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(holder1.itemView.getContext());
                    final LayoutInflater inflater = getInflater();
                    final View dialogView = inflater.inflate(R.layout.edit_safe_zone, null);
                    final TextView name = (TextView) dialogView.findViewById(R.id.name);
                    final TextView latitude = (TextView) dialogView.findViewById(R.id.latitude);
                    final TextView longitude = (TextView) dialogView.findViewById(R.id.longitude);
                    final TextView radius = (TextView) dialogView.findViewById(R.id.radius);

                    name.setText(safeZone.getName());
                    latitude.setText(String.valueOf(safeZone.getLatLng().latitude));
                    longitude.setText(String.valueOf(safeZone.getLatLng().longitude));
                    radius.setText(String.valueOf(safeZone.getRadius()));

                    builder.setTitle("Edit Safezone: " + safeZone.getName());
                    builder.setView(dialogView);
                    builder.setCancelable(false);

                    builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            editSafeZone(
                                    String.valueOf(safeZone.getId()),
                                    name.getText().toString(),
                                    latitude.getText().toString(),
                                    longitude.getText().toString(),
                                    radius.getText().toString());
                        }
                    });

                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });
                    final Dialog dialog = builder.create();
                    dialog.show();
                }
            });


            holder1.btnDeleteSafeZone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(holder1.itemView.getContext());
                    final LayoutInflater inflater = getInflater();
                    builder.setTitle("Delete Safezone: " + safeZone.getName());
                    builder.setCancelable(false);

                    builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            deleteSafeZone(String.valueOf(safeZone.getId()));
                        }
                    });

                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });
                    final Dialog dialog = builder.create();
                    dialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return safeZonesList.size();
        }

        public SafeZone getItem(final int position) {
            return safeZonesList.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView name;
            public TextView latitude;
            public TextView longitude;
            public TextView radius;
            public Button btnUpdateSafeZone;
            public Button btnDeleteSafeZone;

            public ViewHolder(final View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                latitude = (TextView) itemView.findViewById(R.id.latitude);
                longitude = (TextView) itemView.findViewById(R.id.longitude);
                radius = (TextView) itemView.findViewById(R.id.radius);
                btnUpdateSafeZone = (Button) itemView.findViewById(R.id.btnUpdateSafeZone);
                btnDeleteSafeZone = (Button) itemView.findViewById(R.id.btnDeleteSafeZone);
            }
        }
    }

    private void editSafeZone(
            final String safe_zone_id,
            final String name,
            final String latitude,
            final String longitude,
            final String radius) {
        // Tag used to cancel the request
        final String tag_string_req = "req_edit_safe_zone";
        pDialog.setMessage("Editing a Safe Zone ...");
        setLoading(true);
        final String url = session.getServerUrl() + Config.URL_EDIT_SAFE_ZONE;
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Log.e(TAG, "Editing a Safe Zone: " + response);
                setLoading(false);
                try {
                    final JSONObject jObj = new JSONObject(response);
                    final boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(ViewAllSafeZonesActivity.this,
                                "Successfully updated a new safe zone",
                                Toast.LENGTH_LONG).show();

                        final Gson gson = new GsonBuilder().create();
                        final Type type = new TypeToken<SafeZoneJson>() {
                        }.getType();
                        final JSONObject safezone = jObj.getJSONObject("safezone");
                        final SafeZoneJson geo = gson.fromJson(safezone.toString(), type);
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(final Realm realm) {
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
                        });
                        final Intent intent = new Intent(ViewAllSafeZonesActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        final StringBuilder stringBuilder = new StringBuilder();
                        // Error in login. Get the error message
                        final Boolean errorStatus = jObj.getBoolean("error");
                        String errorMsg = jObj.getString("code");
                        final JSONObject messages = jObj.getJSONObject("messages");
                        if (messages.has("safe_zone_id")) {
                            final String safe_zone_id = messages.getJSONArray("safe_zone_id").get(0).toString();
                            stringBuilder.append("\n").append(safe_zone_id);
                        }
                        if (messages.has("name")) {
                            final String name = messages.getJSONArray("name").get(0).toString();
                            stringBuilder.append("\n").append(name);
                        }

                        if (messages.has("latitude")) {
                            final String latitude = messages.getJSONArray("latitude").get(0).toString();
                            stringBuilder.append("\n").append(latitude);
                        }

                        if (messages.has("longitude")) {
                            final String longitude = messages.getJSONArray("longitude").get(0).toString();
                            stringBuilder.append("\n").append(longitude);
                        }

                        if (messages.has("radius")) {
                            final String radius = messages.getJSONArray("radius").get(0).toString();
                            stringBuilder.append("\n").append(radius);
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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                setLoading(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("safe_zone_id", safe_zone_id);
                params.put("name", name);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("radius", radius);
                return params;
            }
        };

        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void deleteSafeZone(final String safe_zone_id) {
        // Tag used to cancel the request
        final String tag_string_req = "req_delete_safe_zone";
        pDialog.setMessage("Deleting a Safe Zone ...");
        setLoading(true);
        final String url = session.getServerUrl() + Config.URL_DELETE_SAFE_ZONE;
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Log.e(TAG, "Deleting a Safe Zone: " + response);
                setLoading(false);
                try {
                    final JSONObject jObj = new JSONObject(response);
                    final boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(ViewAllSafeZonesActivity.this,
                                "Successfully deleted a new safe zone",
                                Toast.LENGTH_LONG).show();
                        final long safe_zone_id = jObj.getLong("safe_zone_id");
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(final Realm realm) {
                                SafeZone safe = SafeZone.query(realm).byId(safe_zone_id);
                                if (!safe.equals(null)) {
                                    safe.deleteFromRealm();
                                }
                            }
                        });
                        final Intent intent = new Intent(ViewAllSafeZonesActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        final StringBuilder stringBuilder = new StringBuilder();
                        // Error in login. Get the error message
                        final Boolean errorStatus = jObj.getBoolean("error");
                        String errorMsg = jObj.getString("code");
                        final JSONObject messages = jObj.getJSONObject("messages");
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
                Log.e(TAG, "Delete Safe Zone Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                setLoading(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("safe_zone_id", safe_zone_id);
                return params;
            }
        };

        // Adding request to request queue
        App.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
