package com.example.simstatenotifier.helper

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.simstatenotifier.constants.ApplicationConstants
import com.example.simstatenotifier.sharedpreferences.SharedPreferencesForData

class SimDataHelper(
    private val activity: Activity,
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

    private var firstSimNewLiveData = MutableLiveData<HashMap<String?, String?>>()

    private var secondSimNewLiveData = MutableLiveData<HashMap<String?, String?>>()


    init {
        setSimData()
    }

    fun getSelectedSimStatus(subscriptionId: String?): MutableLiveData<String> {
        var simCount = sharedPreferences.getValueInt(ApplicationConstants.KEY_NEW_SIM_COUNT)
        var selectedSimStatusValue = MutableLiveData<String>()
        firstSimNewLiveData.observeForever {
            selectedSimStatusValue.value = returnSelectedSimStatus(subscriptionId, simCount)
        }
        secondSimNewLiveData.observeForever {
            selectedSimStatusValue.value = returnSelectedSimStatus(subscriptionId, simCount)
        }
        return selectedSimStatusValue
    }

    private fun returnSelectedSimStatus(subscriptionId: String?, simCount: Int): String? =
        when (simCount) {
            0 -> ApplicationConstants.BOTH_SLOT_EMPTY
            1 -> returnSelectedSimStatusForSingleSim(
                subscriptionId,
                sharedPreferences.getValueInt(ApplicationConstants.KEY_NEW_SIM_SLOT_INDEX)
            )
            2 -> returnSelectedSimStatusForDualSim(subscriptionId)
            else -> ""
        }

    private fun returnSelectedSimStatusForSingleSim(
        subscriptionId: String?,
        simSlotIndex: Int?
    ): String? = if (simSlotIndex == 0) {
        when {
            (firstSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == subscriptionId &&
                    firstSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == firstSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID]) ->
                ApplicationConstants.SIM_PRESENT_NO_CHANGE
            (firstSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == subscriptionId &&
                    firstSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == secondSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID]) ->
                ApplicationConstants.SIM_PRESENT_SLOT_CHANGE
            (firstSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] != subscriptionId) ->
                ApplicationConstants.SIM_CHANGE
            else -> ""
        }
    } else if (simSlotIndex == 1) {
        when {
            (secondSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == subscriptionId &&
                    secondSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == secondSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID]) ->
                ApplicationConstants.SIM_PRESENT_NO_CHANGE
            (secondSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == subscriptionId &&
                    secondSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == firstSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID]) ->
                ApplicationConstants.SIM_PRESENT_SLOT_CHANGE
            (secondSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] != subscriptionId) ->
                ApplicationConstants.SIM_CHANGE
            else -> ""
        }
    } else {
        ""
    }

    private fun returnSelectedSimStatusForDualSim(subscriptionId: String?) = when {
        ((firstSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] == subscriptionId &&
                firstSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] == firstSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID]) ||
                secondSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] == subscriptionId &&
                secondSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] == secondSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID]) -> {
            ApplicationConstants.SIM_PRESENT_NO_CHANGE
        }
        ((firstSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] == subscriptionId &&
                firstSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] == secondSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID]) ||
                secondSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] == subscriptionId &&
                secondSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] == firstSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID]) -> {
            ApplicationConstants.SIM_PRESENT_SLOT_CHANGE
        }
        (firstSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] != subscriptionId &&
                secondSimNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] != subscriptionId) -> {
            ApplicationConstants.SIM_CHANGE
        }
        else -> ""
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
            secondSimOld =
                sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_SECOND_OLD)
        }
        firstSimNewLiveData.value = firstSimNew
        secondSimNewLiveData.value = secondSimNew
    }

    fun setNewSimData() {
        firstSimNew = sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_FIRST)
        secondSimNew = sharedPreferences.getHashMapData(ApplicationConstants.KEY_SLOT_SECOND)
        firstSimNewLiveData.value = firstSimNew
        secondSimNewLiveData.value = secondSimNew
    }

    fun setNewSimDataToSharedPreferences() {
        if (checkPermission())
            setSimDataHelper.setNewSimData()
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
                activity,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.READ_PHONE_STATE),
                PERMISSION_READ_STATE
            )
        }
        return false
    }
}
