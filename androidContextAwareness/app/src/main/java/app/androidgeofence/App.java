package app.androidgeofence;


import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {
    private static App sApplication;
    public static final String TAG = App.class.getSimpleName();

    public static Application getApplication() {
        return sApplication;
    }

    private RequestQueue mRequestQueue;

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        // Initialize Fabric with the debug-disabled Crashlytics
        // See https://docs.fabric.io/android/crashlytics/build-tools.html#disabling-crashlytics-for-debug-builds
        //        Fabric.with(this, new Crashlytics.Builder()
        //                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
        //                .build());
        //        //setStrictMode();
        //        initializeTimber();
        //
        //        RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
        //            @Override
        //            public void handleError(Throwable e) {
        //                super.handleError(e);
        //                Timber.e(e.toString());
        //            }
        //        });
        // Build the RealmConfiguration and set it to the default.
        Realm.setDefaultConfiguration(getRealmConfig());
    }

    public static synchronized App getInstance() {
        return sApplication;
    }

    public static RealmConfiguration getRealmConfig() {
        // Build the RealmConfiguration and set it to the default.
        final RealmConfiguration config = new RealmConfiguration.Builder(getContext())
                .name("geofence.realm")
                .schemaVersion(sApplication.getResources().getInteger(R.integer.db_version))
                .deleteRealmIfMigrationNeeded()
                //.migration(new GeofenceSchemaMigration())
                // Migration to run instead of throwing an exception
                .build();

        if (BuildConfig.DEBUG) {
            // Log whether the RealmConfiguration exists. To view this message in LogCat, you may
            // need to change the filter from "Show only selected application" to "No Filters" and
            // search for the TAG.
            //Log.e(TAG, new File(config.getPath()).exists() ?
            //        "RealmConfiguration exists." :
            //        "RealmConfiguration does not exist.");
        }

        return config;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(
                    getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(final Request<T> req,
                                      String tag) {
        // set the default tag if tag is empty
        req.setRetryPolicy(
                new DefaultRetryPolicy(
                        6 * 2000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
