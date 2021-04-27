package com.example.simstatenotifier

import android.content.Context
import android.os.Build
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import androidx.annotation.RequiresPermission

class SimDataHelper(context: Context) {

    private val sManager =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP_MR1")
        }

    private val sharedPreference = SharedPreferencesForData(context)

    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    fun setOldSimData() {
        val infoSim1: SubscriptionInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            sManager.getActiveSubscriptionInfoForSimSlotIndex(0)
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP_MR1")
        }
        val infoSim2: SubscriptionInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            sManager.getActiveSubscriptionInfoForSimSlotIndex(1)
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP_MR1")
        }
        if (infoSim1 != null) {
            sharedPreference.saveHashMap(
                ApplicationConstants.KEY_SLOT_FIRST_OLD,
                infoSim1.displayName.toString(),
                infoSim1.subscriptionId.toString(),
                infoSim1.number
            )
        } else {
            sharedPreference.saveHashMap(
                ApplicationConstants.KEY_SLOT_FIRST_OLD,
                ApplicationConstants.NOT_SET,
                ApplicationConstants.NOT_SET,
                ApplicationConstants.NOT_SET
            )
        }
        if (infoSim2 != null) {
            sharedPreference.saveHashMap(
                ApplicationConstants.KEY_SLOT_SECOND_OLD,
                infoSim2.displayName.toString(),
                infoSim2.subscriptionId.toString(),
                infoSim2.number
            )
        } else {
            sharedPreference.saveHashMap(
                ApplicationConstants.KEY_SLOT_SECOND_OLD,
                ApplicationConstants.NOT_SET,
                ApplicationConstants.NOT_SET,
                ApplicationConstants.NOT_SET
            )
        }

    }

    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    fun setNewSimData() {
        val infoSim1: SubscriptionInfo? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            sManager.getActiveSubscriptionInfoForSimSlotIndex(0)
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP_MR1")
        }
        val infoSim2: SubscriptionInfo? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            sManager.getActiveSubscriptionInfoForSimSlotIndex(1)
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP_MR1")
        }
        if (infoSim1 != null) {
            sharedPreference.saveHashMap(
                ApplicationConstants.KEY_SLOT_FIRST,
                infoSim1.displayName.toString(),
                infoSim1.subscriptionId.toString(),
                infoSim1.number ?: ApplicationConstants.NOT_SET
            )
        } else {
            sharedPreference.saveHashMap(
                ApplicationConstants.KEY_SLOT_FIRST,
                ApplicationConstants.NOT_SET,
                ApplicationConstants.NOT_SET,
                ApplicationConstants.NOT_SET
            )
        }
        if (infoSim2 != null) {
            sharedPreference.saveHashMap(
                ApplicationConstants.KEY_SLOT_SECOND,
                infoSim2.displayName.toString(),
                infoSim2.subscriptionId.toString(),
                infoSim2.number ?: ApplicationConstants.NOT_SET
            )
        } else {
            sharedPreference.saveHashMap(
                ApplicationConstants.KEY_SLOT_SECOND,
                ApplicationConstants.NOT_SET,
                ApplicationConstants.NOT_SET,
                ApplicationConstants.NOT_SET
            )
        }
    }
}