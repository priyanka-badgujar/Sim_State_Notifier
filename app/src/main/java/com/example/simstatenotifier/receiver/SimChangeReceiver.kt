package com.example.simstatenotifier.receiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.simstatenotifier.helper.SetSimDataHelper

//class SimChangeReceiver : BroadcastReceiver() {
//
//    override fun onReceive(context: Context, intent: Intent) {
//        if (ContextCompat.checkSelfPermission(
//                context,
//                Manifest.permission.READ_PHONE_STATE
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            val simDataHelper = SetSimDataHelper(context)
//            simDataHelper.setNewSimData()
//        }
//    }
//}
