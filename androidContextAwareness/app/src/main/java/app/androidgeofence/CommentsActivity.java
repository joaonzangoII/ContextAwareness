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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import app.androidgeofence.json.CommentJson;
import app.androidgeofence.model.User;
import io.realm.Realm;

public class CommentsActivity extends AppCompatActivity {
    private static final String TAG = CommentsActivity.class.getSimpleName();
    private Realm realm;
    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Comments");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        realm = Realm.getDefaultInstance();

        final long user_id = getIntent().getLongExtra("user_id", 1);
        final User user = User.query(realm).byId(user_id);
        if (user == null) {
            logoutUser();
        }

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // session manager
        session = new SessionManager(getApplicationContext());

        fetchCommentsAsync(String.valueOf(user.getId()));
        // usersList = User.query(realm).all().findAll();

       /* RecyclerView allUsersRecyclerView = (RecyclerView) findViewById(R.id.allUsersRecyclerView);
        allUsersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ViewAllUsersActivity.AllUsersAdapter allUsersAdapter = new ViewAllUsersActivity.AllUsersAdapter(this, list);
        allUsersRecyclerView.setAdapter(allUsersAdapter);*/
        //safeZonesList.addChangeListener(this);
    }


    public void fetchCommentsAsync(final String user_id) {
        final String url = session.getServerUrl() + Config.URL_GET_USER_COMMENTS + user_id;
        pDialog.setMessage("Get All Users...");
        final StringRequest stringRequestq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                final Gson gson = new GsonBuilder().create();
                final Type type = new TypeToken<List<CommentJson>>() {
                }.getType();

                final List<CommentJson> list = gson.fromJson(response, type);
                RecyclerView commentsRecyclerView = (RecyclerView) findViewById(R.id.allCommentsRecyclerView);
                commentsRecyclerView.setLayoutManager(new LinearLayoutManager(CommentsActivity.this));
                final CommentsAdapter commentsAdapter = new CommentsAdapter(CommentsActivity.this, list);
                commentsRecyclerView.setAdapter(commentsAdapter);
                //                realm.executeTransaction(new Realm.Transaction() {
                //                    @Override
                //                    public void execute(final Realm realm) {
                //                        for (final UserJson user : list) {
                //                            User.createOrUpdate(realm,
                //                                    user.id,
                //                                    user.firstname,
                //                                    user.middlename,
                //                                    user.lastname,
                //                                    user.id_number,
                //                                    user.email,
                //                                    user.gender,
                //                                    user.date_of_birth,
                //                                    user.user_type,
                //                                    user.password,
                //                                    user.created_at,
                //                                    user.updated_at);
                //                        }
                //                    }
                //                });
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

    public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private Context context;
        private List<CommentJson> commentsList = Collections.emptyList();

        public CommentsAdapter(final Context context,
                               final List<CommentJson> commentsList) {
            this.context = context;
            this.commentsList = commentsList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                          final int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ViewHolder(inflater.inflate(R.layout.comments_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder,
                                     final int position) {
            final ViewHolder holder1 = (ViewHolder) holder;
            final CommentJson comment = getItem(position);
            holder1.title
                    .setText(Html.fromHtml(String.format("<b>Title:</b> %s", comment.title)));
            holder1.safezone
                    .setText(Html.fromHtml(
                            String.format(
                                    "<b>Safe Zone:</b> %s",
                                    comment.getSafeZone(realm, comment.safe_zone_id))));
        }

        @Override
        public int getItemCount() {
            return commentsList.size();
        }

        public CommentJson getItem(final int position) {
            return commentsList.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public TextView safezone;

            public ViewHolder(final View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                safezone = (TextView) itemView.findViewById(R.id.safezone);
            }
        }
    }

    private void logoutUser() {
        session.setLogin(false);
        session.setLoggeInUserId(0);
        // delete all users
        //        realm.executeTransaction(new Realm.Transaction() {
        //            @Override
        //            public void execute(final Realm realm) {
        //                realm.delete(User.class);
        //            }
        //        });
        // Launching the login activity
        final Intent intent = new Intent(CommentsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
