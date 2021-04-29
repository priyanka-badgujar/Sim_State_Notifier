package com.example.simstatenotifier.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.simstatenotifier.helper.SimDataHelper

class MainViewModel(
    private val simDataHelper: SimDataHelper
) : ViewModel() {

    private var firstSimNew = MutableLiveData<HashMap<String?, String?>>()
    private var secondSimNew = MutableLiveData<HashMap<String?, String?>>()
    private var slotOneStatus = MutableLiveData<String>()
    private var slotTwoStatus = MutableLiveData<String>()
    private var simSwappedStatus = MutableLiveData<String>()
    private var selectedSimStatus = MutableLiveData<String>()

    init {
        simDataHelper.setSimData()
        setAndFetchSimData()
    }

    fun setAndFetchSimData() {
        firstSimNew.postValue(simDataHelper.getFirstSimNewData())
        secondSimNew.postValue(simDataHelper.getSecondSimNewData())
        slotOneStatus.postValue(simDataHelper.getFirstSlotStatus())
        slotTwoStatus.postValue(simDataHelper.getSecondSlotStatus())
        simSwappedStatus.postValue(simDataHelper.getSimSwappedStatus())
    }

    fun getFirstSimNewData(): MutableLiveData<HashMap<String?, String?>> {
        return firstSimNew
    }

    fun getSecondSimNewData(): MutableLiveData<HashMap<String?, String?>> {
        return secondSimNew
    }

    fun getSlotOneStatus(): MutableLiveData<String> {
        return slotOneStatus
    }

    fun getSlotTwoStatus(): MutableLiveData<String> {
        return slotTwoStatus
    }

    fun getSimSwappedStatus(): MutableLiveData<String> {
        return simSwappedStatus
    }

    fun setNewSimData() {
        simDataHelper.setNewSimData()
        setAndFetchSimData()
    }

    fun setOldSimDataTONew() {
        simDataHelper.setOldSimDataToNew()
        setAndFetchSimData()
    }

    fun initialiseData() {
        simDataHelper.setSimData()
        setAndFetchSimData()
    }

    fun getSelectedSimStatus(selectedSubscriptionId: String?): MutableLiveData<String> {
        return simDataHelper.getSelectedSimStatus(selectedSubscriptionId)
    }
}
