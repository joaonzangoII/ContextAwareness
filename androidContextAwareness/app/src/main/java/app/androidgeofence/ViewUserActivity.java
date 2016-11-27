package app.androidgeofence;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import app.androidgeofence.model.User;
import io.realm.Realm;
import io.realm.RealmResults;

public class ViewUserActivity extends AppCompatActivity {
    private SessionManager session;
    private Realm realm;
    private RealmResults<User> usersList;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        realm = Realm.getDefaultInstance();
        // session manager
        session = new SessionManager(getApplicationContext());
        final long user_id = getIntent().getLongExtra("user_id", 1);
        final User user = User.query(realm).byId(user_id);
        if (user == null) {
            final LoginActivity login = new LoginActivity();
            login.logoutUser(ViewUserActivity.this, session);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(user.getFullname());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        final ImageView ImageView = (ImageView) findViewById(R.id.image);
        Glide.with(ViewUserActivity.this).load(user.getPicture_url(session))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ImageView);
        final TextView fullname = (TextView) findViewById(R.id.fullname);
        final TextView idNumber = (TextView) findViewById(R.id.id_number);
        final TextView gender = (TextView) findViewById(R.id.gender);
        final TextView dateOfBirth = (TextView) findViewById(R.id.date_of_birth);

        fullname.setText(Html.fromHtml(String.format("<b>Full name:</b> %s", user.getFullname())));
        idNumber.setText(Html.fromHtml(String.format("<b>Id Number:</b> %s", user.getIdNumber())));
        dateOfBirth.setText(Html.fromHtml(String.format("<b>Date of birth:</b> %s", user.getDateOfBirth())));
        gender.setText(Html.fromHtml(String.format("<b>Gender:</b> %s", user.getGender())));
    }
}
