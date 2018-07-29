package com.cashzhang.ashley;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class Settings extends Application {

    private static Context context;

    public static Context getAppContext() {
        if (context != null)
            return context;
        else {
            context = Constants.s_activity;
            return context;
        }
    }

    public static SharedPreferences getPreferences(){
        return Settings.getAppContext().getSharedPreferences("ashley", 0);
    }

    public static void setAccessToken(String tokenString) { getPreferences().edit().putString("access_token", tokenString).apply(); }
    public static void setRefreshToken(String tokenString) { getPreferences().edit().putString("refresh_token", tokenString).apply(); }
    public static void setGivenName(String name) { getPreferences().edit().putString("given_name", name).apply(); }
    public static void setEmail(String email) { getPreferences().edit().putString("email", email).apply(); }
    public static void setId(String id) { getPreferences().edit().putString("id", id).apply(); }
    public static void setCategId(String categId) { getPreferences().edit().putString("categId", categId).apply(); }
    public static void setCategLabel(String categLabel) { getPreferences().edit().putString("categLabel", categLabel).apply(); }

    public static String getAccessToken() { return getPreferences().getString("access_token", ""); }
    public static String getRefreshToken() { return getPreferences().getString("refresh_token", ""); }
    public static String getGivenName() { return getPreferences().getString("given_name", ""); }
    public static String getEmail() { return getPreferences().getString("email", ""); }
    public static String getId() { return getPreferences().getString("id", ""); }
    public static String getCategId() { return getPreferences().getString("categId", ""); }
    public static String getCategLabel() { return getPreferences().getString("categLabel", ""); }

}
