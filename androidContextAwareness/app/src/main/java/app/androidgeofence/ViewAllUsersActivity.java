package app.androidgeofence;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import app.androidgeofence.json.UserJson;
import app.androidgeofence.model.User;
import io.realm.Realm;
import io.realm.RealmResults;

public class ViewAllUsersActivity extends AppCompatActivity {
    private static final String TAG = ViewAllUsersActivity.class.getSimpleName();
    private SessionManager session;
    private Realm realm;
    private RealmResults<User> usersList;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("All Users");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        realm = Realm.getDefaultInstance();
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        setLoading(true);
        // session manager
        session = new SessionManager(getApplicationContext());
        RecyclerView allUsersRecyclerView = (RecyclerView) findViewById(R.id.allUsersRecyclerView);
        allUsersRecyclerView.setLayoutManager(new LinearLayoutManager(ViewAllUsersActivity.this));

        fetchUsersAsync();
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

    public class AllUsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;
        private SessionManager session;
        private List<User> usersList = Collections.emptyList();

        public AllUsersAdapter(final Context context,
                               final SessionManager session,
                               final List<User> usersList) {
            this.context = context;
            this.session = session;
            this.usersList = usersList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                          final int viewType) {

            final View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.users_list_item, parent, false);
            final ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder,
                                     final int position) {
            final ViewHolder holder1 = (ViewHolder) holder;
            final User user = getItem(position);
            if (user != null) {
                Log.e("Image", user.getPicture_url(this.session));
                final ImageView profilePicture =
                        (ImageView) holder1.itemView.findViewById(R.id.profilePicture);
                //                Picasso.with(context.getApplicationContext())
                //                        .load(user.getPicture_url(this.session))
                //                        .placeholder(R.drawable.user)
                //                        .error(R.drawable.user)
                //                        .into(profilePicture);
                Glide.with(holder1.itemView.getContext())
                        .load(user.getPicture_url(this.session))
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(final Exception e,
                                                       final String model,
                                                       Target<GlideDrawable> target,
                                                       boolean isFirstResource) {
                                Log.e("IMAGE_EXCEPTION", "Exception " + e.toString());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource,
                                                           String model,
                                                           Target<GlideDrawable> target,
                                                           boolean isFromMemoryCache,
                                                           boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(profilePicture);
                holder1.fullname
                        .setText(Html.fromHtml(String.format("<b>Full name:</b> %s", user.getFullname())));
                holder1.gender
                        .setText(Html.fromHtml(String.format("<b>Gender:</b> %s", String.valueOf(user.getGender()))));
                holder1.dateOfBirth
                        .setText(Html.fromHtml(String.format("<b>Date of Birth:</b> %s", String.valueOf(user.getDateOfBirth()))));

                holder1.btnViewUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final Intent intent = new Intent(ViewAllUsersActivity.this, ViewUserActivity.class);
                        intent.putExtra("user_id", user.getId());
                        startActivity(intent);
                    }
                });

                holder1.btnViewComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        final Intent intent = new Intent(ViewAllUsersActivity.this, CommentsActivity.class);
                        intent.putExtra("user_id", user.getId());
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return usersList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public User getItem(final int position) {
            return usersList.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView profilePicture;
            public TextView fullname;
            public TextView gender;
            public TextView dateOfBirth;
            public TextView radius;
            public Button btnViewUser;
            public Button btnViewComments;

            public ViewHolder(final View itemView) {
                super(itemView);
                profilePicture = (ImageView) findViewById(R.id.profilePicture);
                fullname = (TextView) itemView.findViewById(R.id.fullname);
                gender = (TextView) itemView.findViewById(R.id.gender);
                dateOfBirth = (TextView) itemView.findViewById(R.id.dateOfBirth);
                radius = (TextView) itemView.findViewById(R.id.radius);
                btnViewUser = (Button) itemView.findViewById(R.id.btnViewUser);
                btnViewComments = (Button) itemView.findViewById(R.id.btnViewComments);
            }
        }
    }

    public void fetchUsersAsync() {
        final String url = session.getServerUrl() + Config.URL_GET_USERS;
        pDialog.setMessage("Get All Users...");
        final StringRequest stringRequestq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                final Gson gson = new GsonBuilder().create();
                final Type type = new TypeToken<List<UserJson>>() {
                }.getType();

                final List<UserJson> list = gson.fromJson(response, type);
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(final Realm realm) {
                        for (final UserJson user : list) {
                            User.createOrUpdate(realm,
                                    user.id,
                                    user.firstname,
                                    user.middlename,
                                    user.lastname,
                                    user.id_number,
                                    user.email,
                                    user.gender,
                                    user.date_of_birth,
                                    user.user_type,
                                    user.picture_url,
                                    user.password,
                                    user.created_at,
                                    user.updated_at);
                        }
                    }
                });

                usersList = User.query(realm).all().findAll();
                //safeZonesList.addChangeListener(this);
                RecyclerView allUsersRecyclerView = (RecyclerView) findViewById(R.id.allUsersRecyclerView);
                allUsersRecyclerView.setLayoutManager(new LinearLayoutManager(ViewAllUsersActivity.this));
                final AllUsersAdapter allUsersAdapter =
                        new AllUsersAdapter(ViewAllUsersActivity.this, session, usersList);
                allUsersRecyclerView.setAdapter(allUsersAdapter);
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
}
