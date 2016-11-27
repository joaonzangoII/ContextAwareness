package app.androidgeofence;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.androidgeofence.json.SafeZoneJson;
import app.androidgeofence.model.SafeZone;
import io.realm.Realm;

/**
 * A login screen that offers login via email/password.
 */
public class AddSafeZoneActivity extends AppCompatActivity {
    private static final String TAG = AddSafeZoneActivity.class.getSimpleName();
    private EditText inputName;
    private EditText inputLatitude;
    private EditText inputLongitude;
    private EditText inputRadius;
    private ProgressDialog pDialog;
    private SessionManager session;
    private Realm realm;
    private Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_safe_zone);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Safe Zone");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        inputName = (EditText) findViewById(R.id.name);
        inputLatitude = (EditText) findViewById(R.id.latitude);
        inputLongitude = (EditText) findViewById(R.id.longitude);
        inputRadius = (EditText) findViewById(R.id.radius);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        realm = Realm.getDefaultInstance();

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Link to Register Screen
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                final String name = inputName.getText().toString().trim();
                final String latitude = inputLatitude.getText().toString().trim();
                final String longitude = inputLongitude.getText().toString().trim();
                final String radius = inputRadius.getText().toString().trim();
                // Check for empty data in the form
                addSafeZone(
                        name,
                        latitude,
                        longitude,
                        radius);
            }
        });
    }

    /**
     * function to verify login details to realm
     * firstname,middlename, idNumber, lastname,email, password, passwordConfirm
     */
    private void addSafeZone(
            final String name,
            final String latitude,
            final String longitude,
            final String radius) {
        // Tag used to cancel the request
        final String tag_string_req = "req_add_safe_zone";
        pDialog.setMessage("Adding a Safe Zone ...");
        setLoading(true);
        final String url = session.getServerUrl() + Config.URL_ADD_SAFE_ZONE;
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Log.e(TAG, "Adding a Safe Zone: " + response);
                setLoading(false);
                try {
                    final JSONObject jObj = new JSONObject(response);
                    final boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(AddSafeZoneActivity.this,
                                "Successfully added a new safe zone",
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
                        final Intent intent = new Intent(AddSafeZoneActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        final StringBuilder stringBuilder = new StringBuilder();
                        // Error in login. Get the error message
                        final Boolean errorStatus = jObj.getBoolean("error");
                        String errorMsg = jObj.getString("code");
                        final JSONObject messages = jObj.getJSONObject("messages");
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
                Log.e(TAG, "Adding Safe Zone Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                setLoading(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
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


    private void setLoading(final boolean isLoading) {
        if (isLoading) {
            pDialog.show();
        } else {
            pDialog.dismiss();
        }
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
}

