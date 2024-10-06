package com.example.courierapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferancesManager {
    //the constants
    private static final String SHARED_PREF_NAME = "metropsharedpref";
    private static final String KEY_USERROLE = "keyuserrole";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_ID = "keyid";

    private static SharedPreferancesManager mInstance;
    private static Context mCtx;

    private SharedPreferancesManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPreferancesManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPreferancesManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(UserInformation user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getUserId());
        editor.putString(KEY_USERROLE, user.getUserRole());
        //Log.d("MESSAGE",user.getUserRole());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERROLE, null) != null;
    }

    //this method will give the logged in user
    public UserInformation getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new UserInformation(
                sharedPreferences.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_USERROLE, null)
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginPage.class));
    }
}
