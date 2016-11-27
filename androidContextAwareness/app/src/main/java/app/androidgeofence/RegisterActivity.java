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

import io.realm.Realm;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText inputFirstName;
    private EditText inputMiddleName;
    private EditText inputLastName;
    private EditText inputIdNumber;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputPasswordConfirmation;
    // private EditText inputServerAddress;
    private ProgressDialog pDialog;
    private SessionManager session;
    private Realm realm;
    private Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Registration");
        }
        inputFirstName = (EditText) findViewById(R.id.firstname);
        inputMiddleName = (EditText) findViewById(R.id.middlename);
        inputLastName = (EditText) findViewById(R.id.lastname);
        inputIdNumber = (EditText) findViewById(R.id.id_number);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputPasswordConfirmation = (EditText) findViewById(R.id.password_confirmation);
        // inputServerAddress = (EditText) findViewById(R.id.server_address);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        realm = Realm.getDefaultInstance();

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Set Initial amount for the Server Address
        // inputServerAddress.setText(session.getServerUrl());

        // Link to Register Screen
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                final String firstname = inputFirstName.getText().toString().trim();
                final String middlename = inputMiddleName.getText().toString().trim();
                final String lastname = inputLastName.getText().toString().trim();
                final String idNumber = inputIdNumber.getText().toString().trim();
                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                final String passwordConfirm = inputPasswordConfirmation.getText().toString().trim();
                // final String serverAdress = inputServerAddress.getText().toString().trim();
                // Check for empty data in the form
                //                if (!serverAdress.isEmpty()) {
                //                    // login user
                //                    session.setServerUrl(serverAdress);
                registerUser(firstname, middlename, lastname, idNumber, email, password, passwordConfirm);
                /*} else {
                    // Prompt user to enter credentials
                    Toast.makeText(RegisterActivity.this,
                            "Please Set the server Url!",
                            Toast.LENGTH_LONG).show();
                }*/
            }
        });
    }

    /**
     * function to verify login details to realm
     * firstname,middlename, idNumber, lastname,email, password, passwordConfirm
     */
    private void registerUser(final String firstname,
                              final String middlename,
                              final String lastname,
                              final String idNumber,
                              final String email,
                              final String password,
                              final String passwordConfirm) {
        // Tag used to cancel the request
        final String tag_string_req = "req_register";
        pDialog.setMessage("Registering user ...");
        setLoading(true);
        final String url = session.getServerUrl() + Config.URL_REGISTER;
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Log.e(TAG, "Register Response: " + response);
                setLoading(false);
                try {
                    final JSONObject jObj = new JSONObject(response);
                    final boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        // session.setLogin(true);
                        final JSONObject user = jObj.getJSONObject("user");
                        final String firstname = user.getString("firstname");
                        final String middlename = user.getString("middlename");
                        final String lastname = user.getString("lastname");
                        final String idNumber = user.getString("id_number");
                        final String email = user.getString("email");
                        final String gender = user.getString("gender");
                        final String date_of_birth = user.getString("date_of_birth");
                        final String user_type = user.getString("user_type");
                        final String created_at = user.getString("created_at");
                        final String updated_at = user.getString("updated_at");
                        //                        final String name = user.getString("name");
                        //                        final String email = user.getString("email");
                        //                        final String created_at = user.getString("created_at");
                        //
                        //                        // Inserting row in users table
                        //                        realm.executeTransaction(new Realm.Transaction() {
                        //                            @Override
                        //                            public void execute(Realm realm) {
                        //                                User.createOrUpdate(realm, name, email, created_at);
                        //                            }
                        //                        });

                        Toast.makeText(RegisterActivity.this,
                                "Your registration was successful",
                                Toast.LENGTH_LONG).show();
                        // Launch map activity
                        final Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
                            inputFirstName.setError(firstname);
                            inputFirstName.setError(firstname);
                        } else {
                            inputFirstName.setError(null);
                            inputFirstName.setError(null);
                        }

                        if (messages.has("lastname")) {
                            final String lastname = messages.getJSONArray("lastname").get(0).toString();
                            stringBuilder.append("\n").append(lastname);
                            inputLastName.setError(lastname);
                            inputLastName.setError(lastname);
                        } else {
                            inputLastName.setError(null);
                            inputLastName.setError(null);
                        }

                        if (messages.has("email")) {
                            final String email = messages.getJSONArray("email").get(0).toString();
                            stringBuilder.append("\n").append(email);
                            inputEmail.setError(email);
                            inputEmail.setError(email);
                        } else {
                            inputEmail.setError(null);
                            inputEmail.setError(null);
                        }

                        if (messages.has("id_number")) {
                            final String idNumber = messages.getJSONArray("id_number").get(0).toString();
                            stringBuilder.append("\n").append(idNumber);
                            inputIdNumber.setError(idNumber);
                            inputIdNumber.setError(idNumber);
                        } else {
                            inputPassword.setError(null);
                            inputPasswordConfirmation.setError(null);
                        }

                        if (messages.has("password")) {
                            final String password = messages.getJSONArray("password").get(0).toString();
                            stringBuilder.append("\n").append(password);
                            if(password.contains("confirmation")) {
                                inputPasswordConfirmation.setError(password);
                            }else{
                                inputPassword.setError(password);
                            }
                        } else {
                            inputPassword.setError(null);
                            inputPasswordConfirmation.setError(null);
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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                setLoading(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();
                params.put("firstname", firstname);
                params.put("middlename", middlename);
                params.put("lastname", lastname);
                params.put("id_number", idNumber);
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
                final Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            case R.id.action_logout: {

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}

