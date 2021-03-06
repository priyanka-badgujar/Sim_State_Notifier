package com.example.simstatenotifier

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.simstatenotifier.constants.ApplicationConstants
import com.example.simstatenotifier.helper.SetSimDataHelper
import com.example.simstatenotifier.helper.SimDataHelper
import com.example.simstatenotifier.sharedpreferences.SharedPreferencesForData
import com.example.simstatenotifier.viewmodel.MainViewModel
import com.example.simstatenotifier.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val sharedPreferences by lazy {
        SharedPreferencesForData(
            this
        )
    }
    private val setSimDataHelper by lazy {
        SetSimDataHelper(
            this
        )
    }
    private lateinit var mainViewModel: MainViewModel
    private val PERMISSION_READ_STATE = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermission()
        setupViewModel()
        setupObserver()
        checkAndSetIfSimIsSelected()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(
                SimDataHelper(this, sharedPreferences, setSimDataHelper)
            )
        ).get(MainViewModel::class.java)
    }

    private fun setupObserver() {
        mainViewModel.getFirstSimNewData().observe(this, Observer {
            mobile_no_slot_1.text =
                "${getString(R.string.mobile_number)} ${it[ApplicationConstants.KEY_MOBILE_NUMBER]}"
            display_name_slot_1.text =
                "${getString(R.string.display_name)} ${it[ApplicationConstants.KEY_DISPLAY_NAME]}"
            subscription_id_slot_1.text =
                "${getString(R.string.subscription_id)} ${it[ApplicationConstants.KEY_SUBSCRIPTION_ID]}"
        })

        mainViewModel.getSlotOneStatus().observe(this, Observer {
            sim_status_slot_1.text = "${getString(R.string.sim_status)} $it"
        })

        mainViewModel.getSecondSimNewData().observe(this, Observer {
            mobile_no_slot_2.text =
                "${getString(R.string.mobile_number)} ${it[ApplicationConstants.KEY_MOBILE_NUMBER]}"
            display_name_slot_2.text =
                "${getString(R.string.display_name)} ${it[ApplicationConstants.KEY_DISPLAY_NAME]}"
            subscription_id_slot_2.text =
                "${getString(R.string.subscription_id)} ${it[ApplicationConstants.KEY_SUBSCRIPTION_ID]}"
        })

        mainViewModel.getSlotTwoStatus().observe(this, Observer {
            sim_status_slot_2.text = "${getString(R.string.sim_status)} $it"
        })
        mainViewModel.getSimSwappedStatus().observe(this, Observer {
            sim_header.text = it
        })
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        mainViewModel.setNewSimData()
        checkAndSetIfSimIsSelected()
    }

    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                PERMISSION_READ_STATE
            )
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_READ_STATE -> if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mainViewModel.initialiseData()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val pref =
            getSharedPreferences(ApplicationConstants.SHARED_PREF_SIM_DATA, Context.MODE_PRIVATE)
        pref.registerOnSharedPreferenceChangeListener(this)
        setNewData.setOnClickListener {
            if (checkPermission()) {
                mainViewModel.setOldSimDataTONew()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.setNewSimDataToSharedPref()
    }

    override fun onStop() {
        super.onStop()
        val pref =
            getSharedPreferences(ApplicationConstants.SHARED_PREF_SIM_DATA, Context.MODE_PRIVATE)
        pref.unregisterOnSharedPreferenceChangeListener(this)
    }

    fun submit(view: View) {
        var id: Int = radio_group.checkedRadioButtonId
        if (id != -1) {
            val radio: RadioButton = findViewById(id)
            var subscriptionId: String? = ""
            var firstSimNew: HashMap<String?, String?> =
                sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_FIRST)
            var secondSimNew: HashMap<String?, String?> =
                sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_SECOND)
            if (radio.text == getString(R.string.sim_1)) {
                subscriptionId = firstSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID]
                sharedPreferences.saveString(ApplicationConstants.KEY_SELECTED_SIM_SUB_ID, firstSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID])
            } else if (radio.text == getString(R.string.sim_2)) {
                subscriptionId = secondSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID]
            }
            sharedPreferences.saveString(ApplicationConstants.KEY_SELECTED_SIM_SUB_ID, subscriptionId)
            checkAndSetIfSimIsSelected()
        } else {
            Toast.makeText(
                this, "Please select SIM from radio buttons",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkAndSetIfSimIsSelected() {
        val subscriptionId = getSelectedSubscriptionId()
        if (subscriptionId != "" && subscriptionId != null) {
            layout_for_sim_selection.visibility = View.GONE
            selected_sim_status_layout.visibility = View.VISIBLE
            selectedSimSubscriptionId.text = "${getString(R.string.subscription_id)} $subscriptionId"
            mainViewModel.getSelectedSimStatus(getSelectedSubscriptionId()).observe(this, Observer {
                selectedSimStatus.text = it
            })
        }
    }

    private fun getSelectedSubscriptionId() : String? =
        sharedPreferences.getValueString(ApplicationConstants.KEY_SELECTED_SIM_SUB_ID)

}
