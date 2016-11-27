package app.androidgeofence;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.androidgeofence.model.User;
import io.realm.Realm;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText inputIdNumber;
    private EditText inputPassword;
    // private EditText inputServerAddress;
    private ProgressDialog pDialog;
    private SessionManager session;
    private Realm realm;

    public LoginActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Login");
        }
        inputIdNumber = (EditText) findViewById(R.id.id_number);
        inputPassword = (EditText) findViewById(R.id.password);
        // inputServerAddress = (EditText) findViewById(R.id.server_address);
        final Button btnLogin = (Button) findViewById(R.id.btnLogin);
        final Button btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        realm = Realm.getDefaultInstance();

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Set Initial amount for the Server Address
        // inputServerAddress.setText(session.getServerUrl());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to map activity
            Intent intent = new Intent(LoginActivity.this, MapActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                // final String serverAdress = inputServerAddress.getText().toString().trim();
                final String id_number = inputIdNumber.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                // Check for empty data in the form
                // if (!id_number.isEmpty() && !password.isEmpty() && !serverAdress.isEmpty()) {
                /*if (!serverAdress.isEmpty()) {
                    // login user
                    session.setServerUrl(serverAdress);*/
                checkLogin(id_number, password);
                /*} else {
                    // Prompt user to enter credentials
                    Toast.makeText(LoginActivity.this,
                            "Please Set the server Url!",
                            Toast.LENGTH_LONG).show();
                }*/
            }
        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * function to verify login details to realm
     */
    private void checkLogin(final String id_number,
                            final String password) {
        // Tag used to cancel the request
        final String tag_string_req = "req_login";
        pDialog.setMessage("Logging in ...");
        setLoading(true);
        final String url = session.getServerUrl() + Config.URL_LOGIN;
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Log.e(TAG, "Login Response: " + response);
                setLoading(false);
                try {
                    final JSONObject jObj = new JSONObject(response);
                    final boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);
                        // Now store the user in SQLite
                        final JSONObject user = jObj.getJSONObject("user");
                        final long id = user.getLong("id");
                        final String firstname = user.getString("firstname");
                        final String middlename = user.getString("middlename");
                        final String lastname = user.getString("lastname");
                        final String idNumber = user.getString("id_number");
                        final String email = user.getString("email");
                        final String gender = user.getString("gender");
                        final String date_of_birth = user.getString("date_of_birth");
                        final String user_type = user.getString("user_type");
                        final String picture_url = user.getString("picture_url");
                        final String created_at = user.getString("created_at");
                        final String updated_at = user.getString("updated_at");
                        session.setLoggeInUserId((int) id);
                        // Inserting row in users table
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                User.createOrUpdate(realm,
                                        id,
                                        firstname,
                                        middlename,
                                        lastname,
                                        idNumber,
                                        email, gender,
                                        date_of_birth,
                                        user_type,
                                        picture_url,
                                        password,
                                        created_at,
                                        updated_at);
                            }
                        });

                        Toast.makeText(LoginActivity.this,
                                "You have successfully been logged in",
                                Toast.LENGTH_LONG).show();
                        // Launch map activity
                        final Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        final StringBuilder stringBuilder = new StringBuilder();
                        // Error in login. Get the error message
                        final Boolean errorStatus = jObj.getBoolean("error");
                        String errorMsg = jObj.getString("code");
                        final JSONObject messages = jObj.getJSONObject("messages");

                        if (messages.has("id_number")) {
                            final String idNumber = messages.getJSONArray("id_number").get(0).toString();
                            stringBuilder.append("\n").append(idNumber);
                            inputIdNumber.setError(idNumber);
                        }else{
                            inputIdNumber.setError(null);
                        }

                        if (messages.has("password")) {
                            final String password = messages.getJSONArray("password").get(0).toString();
                            stringBuilder.append("\n").append(password);
                            inputPassword.setError(password);
                        }else{
                            inputPassword.setError(null);
                        }

                        // Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                setLoading(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("id_number", id_number);
                params.put("password", password);
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

    public static void logoutUser(final Context activity,
                                  final SessionManager session) {
        session.setLogin(false);
        session.setLoggeInUserId(0);
//        // delete all users
//        //        realm.executeTransaction(new Realm.Transaction() {
//        //            @Override
//        //            public void execute(final Realm realm) {
//        //                realm.delete(User.class);
//        //            }
//        //        });
//        // Launching the login activity
        final Intent intent = new Intent(activity, LoginActivity.class);
        //activity.finish();
        activity.startActivity(intent);
    }
}
