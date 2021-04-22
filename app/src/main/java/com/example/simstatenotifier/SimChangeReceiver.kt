package com.example.simstatenotifier

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log

class SimChangeReceiver : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {

        val telephoneMgr =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager


//        val sManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
//        val infoSim1: SubscriptionInfo = sManager.getActiveSubscriptionInfoForSimSlotIndex(0)
//        val infoSim2:SubscriptionInfo = sManager.getActiveSubscriptionInfoForSimSlotIndex(1)
//        Log.i("infoSIM1", infoSim1.toString())
//        Log.i("infoSIM2", infoSim2.toString())

        val sharedPreference = SharedPreferences(context)

        when (telephoneMgr.simState) {
            TelephonyManager.SIM_STATE_ABSENT -> {
                sharedPreference.saveString(
                    ApplicationConstants.KEY_SIM_STATUS,
                    ApplicationConstants.SIM_STATE_ABSENT)
            }

            TelephonyManager.SIM_STATE_NETWORK_LOCKED -> {
                sharedPreference.saveString(
                    ApplicationConstants.KEY_SIM_STATUS,
                    ApplicationConstants.SIM_STATE_NETWORK_LOCKED)
            }

            TelephonyManager.SIM_STATE_PIN_REQUIRED -> {
                sharedPreference.saveString(
                    ApplicationConstants.KEY_SIM_STATUS,
                    ApplicationConstants.SIM_STATE_PIN_REQUIRED)
            }

            TelephonyManager.SIM_STATE_PUK_REQUIRED -> {
                sharedPreference.saveString(
                    ApplicationConstants.KEY_SIM_STATUS,
                    ApplicationConstants.SIM_STATE_PUK_REQUIRED)
            }

            TelephonyManager.SIM_STATE_UNKNOWN -> {
                sharedPreference.saveString(
                    ApplicationConstants.KEY_SIM_STATUS,
                    ApplicationConstants.SIM_STATE_UNKNOWN)
            }

            TelephonyManager.SIM_STATE_READY -> {
                sharedPreference.saveString(
                    ApplicationConstants.KEY_SIM_STATUS,
                    ApplicationConstants.SIM_STATE_READY)

                sharedPreference.saveString(
                    ApplicationConstants.KEY_NETWORK_PROVIDER,
                    telephoneMgr.networkOperatorName)

                sharedPreference.saveString(
                    ApplicationConstants.KEY_MOBILE_NO,
                    telephoneMgr.line1Number)
            }
        }

    }
}