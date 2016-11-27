package app.androidgeofence;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();
    // Shared Preferences
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidGeofenceLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_GET_LOGGED_IN_USER_ID = "loggedInUserId";
    private static final String KEY_SERVER_URL = "server_url";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(final boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        // commit changes
        editor.apply();
        Log.d(TAG, "User login session modified!");
    }

    public void setLoggeInUserId(final int loggedInUserId) {
        editor.putInt(KEY_GET_LOGGED_IN_USER_ID, loggedInUserId);
        // commit changes
        editor.apply();
        Log.d(TAG, "User login session modified!");
    }


    public void setServerUrl(final String serverUrl) {
        editor.putString(KEY_SERVER_URL, serverUrl);
        // commit changes
        editor.apply();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public int getLoggedInUserId(){
        return pref.getInt(KEY_GET_LOGGED_IN_USER_ID, 0);
    }

    public String getServerUrl(){
        return pref.getString(KEY_SERVER_URL, "https://www.contextawareness.lubelnaportal.com");
    }
}
