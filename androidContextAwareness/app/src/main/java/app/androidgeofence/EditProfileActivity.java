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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.androidgeofence.model.User;
import io.realm.Realm;

/**
 * A login screen that offers login via email/password.
 */
public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = EditProfileActivity.class.getSimpleName();
    private EditText inputFirstName;
    private EditText inputMiddleName;
    private EditText inputLastName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputPasswordConfirmation;
    private ProgressDialog pDialog;
    private SessionManager session;
    private Realm realm;
    private Button btnSubmit;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit My profile");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        inputFirstName = (EditText) findViewById(R.id.firstname);
        inputMiddleName = (EditText) findViewById(R.id.middlename);
        inputLastName = (EditText) findViewById(R.id.lastname);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputPasswordConfirmation = (EditText) findViewById(R.id.password_confirmation);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        realm = Realm.getDefaultInstance();
        // Session manager
        session = new SessionManager(getApplicationContext());

        user = User.query(realm).byId((long)session.getLoggedInUserId());
        if (user == null) {
            logoutUser();
        }

        inputFirstName.setText(user.getFirstname());
        inputMiddleName.setText(user.getMiddlename());
        inputLastName.setText(user.getLastname());
        inputEmail.setText(user.getEmail());
        inputPassword.setText(user.getPassword());
        inputPasswordConfirmation.setText(user.getPassword());

        // Link to Register Screen
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                final String firstname = inputFirstName.getText().toString().trim();
                final String middlename = inputMiddleName.getText().toString().trim();
                final String lastname = inputLastName.getText().toString().trim();
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                final String passwordConfirm = inputPasswordConfirmation.getText().toString().trim();
                // Check for empty data in the form
                updateProfile(String.valueOf(user.getId()),
                        firstname,
                        middlename,
                        lastname,
                        email,
                        password,
                        passwordConfirm);
            }
        });
    }

    /**
     * function to verify login details to realm
     * firstname,middlename, idNumber, lastname,email, password, passwordConfirm
     */
    private void updateProfile(
            final String user_id,
            final String firstname,
            final String middlename,
            final String lastname,
            final String email,
            final String password,
            final String passwordConfirm) {
        // Tag used to cancel the request
        final String tag_string_req = "req_register";
        pDialog.setMessage("Registering user ...");
        setLoading(true);
        final String url = session.getServerUrl() + Config.URL_UPDATE_PROFILE;
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Log.e(TAG, "Edit Profile Response: " + response);
                setLoading(false);
                try {
                    final JSONObject jObj = new JSONObject(response);
                    final boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(EditProfileActivity.this,
                                "Your profile has been updated",
                                Toast.LENGTH_LONG).show();
                        // user successfully logged in
                        // Create login session
                        // session.setLogin(true);
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

                        // Launch map activity
                        final Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        final StringBuilder stringBuilder = new StringBuilder();
                        // Error in login. Get the error message
                        final Boolean errorStatus = jObj.getBoolean("error");
                        String errorMsg = jObj.getString("code");
                        final JSONObject messages = jObj.getJSONObject("messages");
                        if (messages.has("firstname")) {
                            final String firstname = messages.getJSONArray("firstname").get(0).toString();
                            stringBuilder.append("\n").append(firstname);
                        }

                        if (messages.has("lastname")) {
                            final String lastname = messages.getJSONArray("lastname").get(0).toString();
                            stringBuilder.append("\n").append(lastname);
                        }

                        if (messages.has("email")) {
                            final String email = messages.getJSONArray("email").get(0).toString();
                            stringBuilder.append("\n").append(email);
                        }

                        if (messages.has("password")) {
                            final String password = messages.getJSONArray("password").get(0).toString();
                            stringBuilder.append("\n").append(password);
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
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                setLoading(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("firstname", firstname);
                params.put("middlename", middlename);
                params.put("lastname", lastname);
                params.put("email", email);
                params.put("password", password);
                params.put("password_confirmation", passwordConfirm);
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

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);
        // delete all users
        //        realm.executeTransaction(new Realm.Transaction() {
        //            @Override
        //            public void execute(final Realm realm) {
        //                realm.delete(User.class);
        //            }
        //        });
        // Launching the login activity
        final Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

