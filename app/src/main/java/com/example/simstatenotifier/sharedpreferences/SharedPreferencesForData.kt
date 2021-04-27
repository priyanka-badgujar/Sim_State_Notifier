package com.example.simstatenotifier.sharedpreferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.simstatenotifier.constants.ApplicationConstants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SharedPreferencesForData(context: Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences(
        ApplicationConstants.SHARED_PREF_SIM_DATA,
        MODE_PRIVATE)

    fun saveHashMap(KEY_SLOT_NO: String, displayName: String, subscriptionId: String, mobileNo: String) {
        val simDataMap = HashMap<String, String>()
        simDataMap[ApplicationConstants.KEY_DISPLAY_NAME] = displayName
        simDataMap[ApplicationConstants.KEY_SUBSCRIPTION_ID] = subscriptionId
        simDataMap[ApplicationConstants.KEY_MOBILE_NUMBER] = mobileNo
        val gson = Gson()
        val hashMapString = gson.toJson(simDataMap)
        sharedPref.edit().putString(KEY_SLOT_NO, hashMapString).apply()
    }

     fun getHashMapData(KEY_NAME: String): HashMap<String?, String?> {
        val defValue = Gson().toJson(HashMap<String, String>())
        val json: String? = sharedPref.getString(KEY_NAME, defValue)
        val token: TypeToken<HashMap<String?, String?>> =
            object : TypeToken<HashMap<String?, String?>>() {}
        return Gson().fromJson(json, token.type)
    }
}
