package app.androidgeofence;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.androidgeofence.model.SafeZone;
import app.androidgeofence.model.User;
import io.realm.Realm;

public class ProfileActivity extends AppCompatActivity
        implements View.OnClickListener {
    private Realm realm;
    private SessionManager session;
    private User user;
    private LinearLayout actionsUser;
    private LinearLayout actionsAdmin;
    private static final String TAG = ProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Profile");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        realm = Realm.getDefaultInstance();
        // session manager
        session = new SessionManager(getApplicationContext());
        user = User.query(realm).byId((long)session.getLoggedInUserId());
        if (user.equals(null)) {
            logoutUser();
        }

        actionsUser = (LinearLayout) findViewById(R.id.actions_user);
        actionsAdmin = (LinearLayout) findViewById(R.id.actions_admin);

        if (user.getUserType().equals("normal")) {
            showUser();
        } else {
            showAdmin();
        }

        final ImageView ImageView = (ImageView) findViewById(R.id.image);
        Glide.with(ProfileActivity.this).load(user.getPicture_url(session))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ImageView);

        final TextView fullname = (TextView) findViewById(R.id.fullname);
        final TextView idNumber = (TextView) findViewById(R.id.id_number);
        final TextView gender = (TextView) findViewById(R.id.gender);
        final TextView dateOfBirth = (TextView) findViewById(R.id.date_of_birth);

        final Button btnGetLocation = (Button) findViewById(R.id.btnGetLocation);
        final Button btnEditInfo = (Button) findViewById(R.id.btnEditUserInformation);
        final Button btnCommment = (Button) findViewById(R.id.btnComment);


        final Button btnAddSfeZonea = (Button) findViewById(R.id.btnAddSafeZOne);
        final Button btnShowSafeZones = (Button) findViewById(R.id.btnViewSafeZones);
        final Button btnGenerateReports = (Button) findViewById(R.id.btnGenerateReports);

        fullname.setText(Html.fromHtml(String.format("<b>Full name:</b> %s", user.getFullname())));
        idNumber.setText(Html.fromHtml(String.format("<b>Id Number:</b> %s", user.getIdNumber())));
        dateOfBirth.setText(Html.fromHtml(String.format("<b>Date of birth:</b> %s", user.getDateOfBirth())));
        gender.setText(Html.fromHtml(String.format("<b>Gender:</b> %s", user.getGender())));

        btnGetLocation.setOnClickListener(this);
        btnEditInfo.setOnClickListener(this);
        btnCommment.setOnClickListener(this);

        btnAddSfeZonea.setOnClickListener(this);
        btnShowSafeZones.setOnClickListener(this);
        btnGenerateReports.setOnClickListener(this);
    }

    public void showUser() {
        actionsUser.setVisibility(View.VISIBLE);
        actionsAdmin.setVisibility(View.GONE);
    }

    public void showAdmin() {
        actionsUser.setVisibility(View.GONE);
        actionsAdmin.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public void onClick(final View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnComment:
                final List<SafeZone> safeZones = SafeZone.query(realm).all().findAll();
                final SafeZonesAdapter safeZonesAdapter =
                        new SafeZonesAdapter(ProfileActivity.this, safeZones);

                final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                final LayoutInflater inflater = getInflater();
                final View dialogView = inflater.inflate(R.layout.comment_box, null);
                final Spinner spinner = (Spinner) dialogView.findViewById(R.id.safeZoneSpinner);
                final TextView txtTitle = (TextView) dialogView.findViewById(R.id.title);
                final TextView txtBody = (TextView) dialogView.findViewById(R.id.body);
                spinner.setAdapter(safeZonesAdapter);
                builder.setTitle("Add Comment");
                builder.setView(dialogView);
                builder.setCancelable(false);

                builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        addComment(txtTitle.getText().toString(),
                                txtBody.getText().toString(),
                                String.valueOf(user.getId()),
                                String.valueOf(((SafeZone) spinner.getSelectedItem()).id));
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
                break;
            case R.id.btnEditUserInformation:
                intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.btnGetLocation:
                intent = new Intent(ProfileActivity.this, MapActivity.class);
                startActivity(intent);
                break;
            case R.id.btnAddSafeZOne:
                intent = new Intent(ProfileActivity.this, AddSafeZoneActivity.class);
                startActivity(intent);
                break;
            case R.id.btnViewSafeZones:
                intent = new Intent(ProfileActivity.this, ViewAllSafeZonesActivity.class);
                startActivity(intent);
                break;
            case R.id.btnGenerateReports:
                intent = new Intent(ProfileActivity.this, ViewAllUsersActivity.class);
                startActivity(intent);
                break;
        }
    }

    public LayoutInflater getInflater() {
        return (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public class SafeZonesAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<SafeZone> safeZones = Collections.emptyList();

        SafeZonesAdapter(final Context context,
                         final List<SafeZone> safeZones) {
            this.safeZones = safeZones;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return safeZones.size();
        }

        @Override
        public SafeZone getItem(int position) {
            return safeZones.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position,
                            View convertView,
                            final ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.safe_zones_spinner_layout, parent, false);
            }

            final SafeZone safezone = getItem(position);
            final TextView txtName = (TextView) convertView.findViewById(R.id.title);
            txtName.setText(safezone.name);
            return convertView;
        }
    }


    /**
     * function to verify login details to realm
     */
    private void addComment(final String title,
                            final String body,
                            final String user_id,
                            final String safe_zone_id) {
        // Tag used to cancel the request
        final String tag_string_req = "add_comment";
        final String url = session.getServerUrl() + Config.URL_COMMENT;
        final StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Log.e(TAG, "Login Response: " + response);
                try {
                    final JSONObject jObj = new JSONObject(response);
                    final boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(ProfileActivity.this,
                                "Your comment has been saved",
                                Toast.LENGTH_LONG).show();
                        //                        // user successfully logged in
                        //                        // Create login session
                        //                        session.setLogin(true);
                        //                        // Now store the user in SQLite
                        //                        final JSONObject user = jObj.getJSONObject("user");
                        //                        final String firstname = user.getString("firstname");
                        //                        final String middlename = user.getString("middlename");
                        //                        final String lastname = user.getString("lastname");
                        //                        final String idNumber = user.getString("id_number");
                        //                        final String email = user.getString("email");
                        //                        final String gender = user.getString("gender");
                        //                        final String date_of_birth = user.getString("date_of_birth");
                        //                        final String user_type = user.getString("user_type");
                        //                        final String created_at = user.getString("created_at");
                        //                        final String updated_at = user.getString("updated_at");
                        //                        // Inserting row in users table
                        //                        realm.executeTransaction(new Realm.Transaction() {
                        //                            @Override
                        //                            public void execute(Realm realm) {
                        //                                User.createOrUpdate(realm,
                        //                                        firstname,
                        //                                        middlename,
                        //                                        lastname,
                        //                                        idNumber,
                        //                                        email,gender,
                        //                                        date_of_birth,
                        //                                        user_type,
                        //                                        created_at,
                        //                                        updated_at);
                        //                            }
                        //                        });
                        //                        // Launch map activity
                        //                        final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        //                        startActivity(intent);
                        //                        finish();
                    } else {
                        final StringBuilder stringBuilder = new StringBuilder();
                        // Error in login. Get the error message
                        final Boolean errorStatus = jObj.getBoolean("error");
                        String errorMsg = jObj.getString("code");
                        final JSONObject messages = jObj.getJSONObject("messages");

                        if (messages.has("body")) {
                            final String body = messages.getJSONArray("body").get(0).toString();
                            stringBuilder.append("\n").append(body);
                        }

                        if (messages.has("title")) {
                            final String title = messages.getJSONArray("title").get(0).toString();
                            stringBuilder.append("\n").append(title);
                        }

                        Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (final JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener()

        {

            @Override
            public void onErrorResponse(final VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                intent = new Intent(ProfileActivity.this, MapActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            case R.id.action_update_profile: {
                intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_logout: {
                logoutUser();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

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
        final Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
