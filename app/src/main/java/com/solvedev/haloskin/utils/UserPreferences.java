package com.solvedev.haloskin.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {

    private static final String PREFERENCES_NAME = "haloskinPref";
    private static final String EMAIL_KEY = "emailKey";
    private static final String TOKEN_KEY = "tokenKey";
    private static final String IS_USER_LOGIN = "UserLogin";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context c;

    public UserPreferences(Context context) {
        c = context;
        pref = c.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSessionUser(String email, String token) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(EMAIL_KEY, email);
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public void deleteLoginSession() {
        editor.remove(EMAIL_KEY);
        editor.remove(TOKEN_KEY);
        editor.remove(IS_USER_LOGIN);
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public String getEmail() {
        return pref.getString(EMAIL_KEY, "");
    }


    public String getToken() {
        return pref.getString(TOKEN_KEY, "");
    }



}
