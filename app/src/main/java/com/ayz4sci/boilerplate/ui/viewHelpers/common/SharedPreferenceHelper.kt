package com.ayz4sci.boilerplate.ui.viewHelpers.common

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by @ayz4sci on 25/07/2018.
 */
class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("com.ayz4sci.boilerplate._shared_prefs",
                    Context.MODE_PRIVATE)

    val editor: SharedPreferences.Editor
        get() = sharedPreferences.edit()

    fun getLongPreference(key: String): Long {
        return sharedPreferences.getLong(key, -1)
    }

    fun getBooleanPreference(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getIntPreference(key: String): Int {
        return sharedPreferences.getInt(key, -1)
    }

    fun getStringPreference(key: String): String {
        return sharedPreferences.getString(key, "")
    }

    // remove shared preferences
    fun removeSharedPreference(key: String) {
        editor.remove(key).commit()
    }

    // remove shared preferences
    fun removeSharedPreferences(vararg keys: String) {
        val editor = editor
        for (key in keys) {
            editor.remove(key)
        }
        editor.commit()
    }

    fun clearPreferences() {
        editor.clear().commit()
    }

    companion object {
        val AUTH_TOKEN = "auth_token"
    }
}