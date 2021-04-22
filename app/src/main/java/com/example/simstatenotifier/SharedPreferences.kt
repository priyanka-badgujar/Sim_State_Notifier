package com.example.simstatenotifier

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {

    val sharedPref: SharedPreferences = context.getSharedPreferences(
        ApplicationConstants.SHARED_PREF_SIM_DATA,
        Context.MODE_PRIVATE)

    fun saveString(KEY_NAME: String, text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, text)
        editor.apply()
    }

    fun saveInt(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_NAME, value)
        editor.apply()
    }
}
