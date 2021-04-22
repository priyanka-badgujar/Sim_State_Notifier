package com.example.simstatenotifier

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
        setObserversForSimData()
    }

    private fun setObserversForSimData() {
        val sharedPref: SharedPreferences = getSharedPreferences(
            ApplicationConstants.SHARED_PREF_SIM_DATA,
            Context.MODE_PRIVATE
        )

        val stringPrefForSimState =
            sharedPref.stringLiveData(ApplicationConstants.KEY_SIM_STATUS, ApplicationConstants.NOT_SET)

        // observer for SIM status
        stringPrefForSimState.observe(this, Observer {
            sim_status.text = ApplicationConstants.SIM_STATE + stringPrefForSimState.value
            if (sim_status.text == ApplicationConstants.SIM_STATE_READY) {
            }
        })

        val stringPrefForNetworkProvider =
            sharedPref.stringLiveData(ApplicationConstants.KEY_NETWORK_PROVIDER, ApplicationConstants.NOT_SET)

        // observer for network provider
        stringPrefForNetworkProvider.observe(this, Observer {
            network_provider.text = ApplicationConstants.NETWORK_PROVIDER +  stringPrefForNetworkProvider.value

        })

        val stringPrefForMobileNo =
            sharedPref.stringLiveData(ApplicationConstants.KEY_MOBILE_NO, ApplicationConstants.NOT_SET)

        // observer for mobile number
        stringPrefForMobileNo.observe(this, Observer {
            mobile_no.text = ApplicationConstants.MOBILE + stringPrefForMobileNo.value

        })

    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            val PERMISSION_READ_STATE = 5
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                PERMISSION_READ_STATE
            )
        }
    }
}
//        buttonSim.setOnClickListener {
//            val sharedPreference = SharedPreference(this)
//
//            val a = sharedPreference.getValueString(ApplicationConstants.SIM_STATUS)
//
//            if (a!= null) {
//                Toast.makeText(
//                    this,
//                    a,
//                    Toast.LENGTH_LONG).show()
//            }
//        }