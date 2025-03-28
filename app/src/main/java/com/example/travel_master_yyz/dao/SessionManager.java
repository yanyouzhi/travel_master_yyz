package com.example.travel_master_yyz.dao;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_ID = "id";
    private static final String KEY_UID = "uid";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserSession(String id, String uid, String token) {
        editor.putString(KEY_ID, id);
        editor.putString(KEY_UID, uid);
        editor.putString(KEY_TOKEN, token);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public void saveUserSession(String id,  String token) {
        editor.putString(KEY_ID, id);
        editor.putString(KEY_TOKEN, token);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public String getId() {
        return sharedPreferences.getString(KEY_ID, null);
    }

    public String getUid() {
        return sharedPreferences.getString(KEY_UID, null);
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false); // 默认值 `false`
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
