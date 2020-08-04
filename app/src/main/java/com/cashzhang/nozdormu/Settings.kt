package com.cashzhang.nozdormu

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

object Settings {
    private var context: Context? = null
    val appContext: Context?
        get() = if (context != null) context else {
            context = Constants.s_activity
            context
        }

    val preferences: SharedPreferences
        get() = appContext!!.getSharedPreferences("nozdormu", 0)

    @JvmStatic
    var accessToken: String?
        get() = preferences.getString("access_token", "")
        set(tokenString) {
            preferences.edit().putString("access_token", tokenString).apply()
        }

    @JvmStatic
    var refreshToken: String?
        get() = preferences.getString("refresh_token", "")
        set(tokenString) {
            preferences.edit().putString("refresh_token", tokenString).apply()
        }

    @JvmStatic
    var givenName: String?
        get() = preferences.getString("given_name", "")
        set(name) {
            preferences.edit().putString("given_name", name).apply()
        }

    @JvmStatic
    var email: String?
        get() = preferences.getString("email", "")
        set(email) {
            preferences.edit().putString("email", email).apply()
        }

    @JvmStatic
    var id: String?
        get() = preferences.getString("id", "")
        set(id) {
            preferences.edit().putString("id", id).apply()
        }

    var categId: String?
        get() = preferences.getString("categId", "")
        set(categId) {
            preferences.edit().putString("categId", categId).apply()
        }

    var categLabel: String?
        get() = preferences.getString("categLabel", "")
        set(categLabel) {
            preferences.edit().putString("categLabel", categLabel).apply()
        }
}