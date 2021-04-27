package com.example.simstatenotifier.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.simstatenotifier.constants.ApplicationConstants
import com.example.simstatenotifier.sharedpreferences.SharedPreferencesForData

class SimDataHelper(
    private val context: Context,
    private val sharedPreferences: SharedPreferencesForData,
    private val setSimDataHelper: SetSimDataHelper
) {
    private var firstSimOld: HashMap<String?, String?> =
        sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_FIRST_OLD)
    private var secondSimOld: HashMap<String?, String?> =
        sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_SECOND_OLD)
    private var firstSimNew: HashMap<String?, String?> =
        sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_FIRST)
    private var secondSimNew: HashMap<String?, String?> =
        sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_SECOND)
    private val PERMISSION_READ_STATE = 5

    init {
       setSimData()
    }

    fun setSimData() {
        if (firstSimNew.size == 0 || secondSimNew.size == 0) {
            if (checkPermission())
                setSimDataHelper.setNewSimData()
            firstSimNew = sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_FIRST)
            secondSimNew = sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_SECOND)
        }

        if (firstSimOld.size == 0 || secondSimOld.size == 0) {
            if (checkPermission())
                setSimDataHelper.setOldSimData()
            firstSimOld = sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_FIRST_OLD)
            secondSimOld = sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_SECOND_OLD)
        }
    }

    fun setNewSimData() {
        if (checkPermission())
            setSimDataHelper.setNewSimData()
        firstSimNew = sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_FIRST)
        secondSimNew = sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_SECOND)
    }

    fun setOldSimDataToNew() {
        if (checkPermission())
            setSimDataHelper.setOldSimData()
        firstSimOld = sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_FIRST_OLD)
        secondSimOld = sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_SECOND_OLD)
    }

    fun getFirstSimNewData(): HashMap<String?, String?> {
        return firstSimNew
    }

    fun getSecondSimNewData(): HashMap<String?, String?> {
        return secondSimNew
    }

    fun getFirstSlotStatus(): String = when {
        (firstSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == ApplicationConstants.NOT_SET) -> {
            ApplicationConstants.SLOT_EMPTY
        }
        (firstSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == firstSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] &&
                firstSimNew[ApplicationConstants.KEY_DISPLAY_NAME] == firstSimOld[ApplicationConstants.KEY_DISPLAY_NAME] &&
                firstSimNew[ApplicationConstants.KEY_MOBILE_NUMBER] == firstSimOld[ApplicationConstants.KEY_MOBILE_NUMBER]) -> {
            ApplicationConstants.NO_CHANGE
        }
        (firstSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == secondSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] &&
                firstSimNew[ApplicationConstants.KEY_DISPLAY_NAME] == secondSimOld[ApplicationConstants.KEY_DISPLAY_NAME] &&
                firstSimNew[ApplicationConstants.KEY_MOBILE_NUMBER] == secondSimOld[ApplicationConstants.KEY_MOBILE_NUMBER]) -> {
            ApplicationConstants.SIM_TWO_POSITION_CHANGE
        }
        else -> {
            ApplicationConstants.NEW_SIM_INSERTED
        }
    }

    fun getSecondSlotStatus(): String = when {
        (secondSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == secondSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] &&
                secondSimNew[ApplicationConstants.KEY_DISPLAY_NAME] == secondSimOld[ApplicationConstants.KEY_DISPLAY_NAME] &&
                secondSimNew[ApplicationConstants.KEY_MOBILE_NUMBER] == secondSimOld[ApplicationConstants.KEY_MOBILE_NUMBER]) -> {
            ApplicationConstants.NO_CHANGE
        }
        (secondSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == firstSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] &&
                secondSimNew[ApplicationConstants.KEY_DISPLAY_NAME] == firstSimOld[ApplicationConstants.KEY_DISPLAY_NAME] &&
                secondSimNew[ApplicationConstants.KEY_MOBILE_NUMBER] == firstSimOld[ApplicationConstants.KEY_MOBILE_NUMBER]) -> {
            ApplicationConstants.SIM_ONE_POSITION_CHANGE
        }
        (secondSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == ApplicationConstants.NOT_SET) -> {
            ApplicationConstants.SLOT_EMPTY
        }
        else -> {
            ApplicationConstants.NEW_SIM_INSERTED
        }
    }

    fun getSimSwappedStatus(): String {
        if (firstSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == secondSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] &&
            secondSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == firstSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] &&
            firstSimNew[ApplicationConstants.KEY_DISPLAY_NAME] == secondSimOld[ApplicationConstants.KEY_DISPLAY_NAME] &&
            secondSimNew[ApplicationConstants.KEY_DISPLAY_NAME] == firstSimOld[ApplicationConstants.KEY_DISPLAY_NAME] &&
            firstSimNew[ApplicationConstants.KEY_MOBILE_NUMBER] == secondSimOld[ApplicationConstants.KEY_MOBILE_NUMBER] &&
            secondSimNew[ApplicationConstants.KEY_MOBILE_NUMBER] == firstSimOld[ApplicationConstants.KEY_MOBILE_NUMBER]
        ) {
            return ApplicationConstants.SIM_SWAPPED
        }
        return ApplicationConstants.SIM_NOT_SWAPPED
    }

    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                PERMISSION_READ_STATE
            )
        }
        return false
    }
}
