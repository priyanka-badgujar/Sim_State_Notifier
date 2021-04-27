package com.example.simstatenotifier

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(
    private val sharedPreferences: SharedPreferencesForData,
    simDataHelper: SimDataHelper
) : ViewModel() {

//    private val users = MutableLiveData<Resource<List<User>>>()
//    private val compositeDisposable = CompositeDisposable()

    private lateinit var firstSimOld: MutableLiveData<HashMap<String?, String?>>
    private lateinit var secondSIMOld: MutableLiveData<HashMap<String?, String?>>
    private lateinit var firstSIMNew: MutableLiveData<HashMap<String?, String?>>
    private lateinit var secondSIMNew: MutableLiveData<HashMap<String?, String?>>

    init {
        fetchSimData()
    }

    private fun fetchSimData() {
        firstSimOld.postValue()
        users.postValue(Resource.loading(null))
        compositeDisposable.add(
            mainRepository.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userList ->
                    users.postValue(Resource.success(userList))
                }, { throwable ->
                    users.postValue(Resource.error("Something Went Wrong", null))
                })
        )
    }

    fun getFirstSimNewData(): MutableLiveData<HashMap<String?, String?>> {
        return firstSIMNew
    }

    fun getSecondSimNewData(): MutableLiveData<HashMap<String?, String?>> {
        return secondSIMNew
    }

    private fun getFirstSimStatus(): String = when {
        (firstSIMNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == firstSimOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] &&
                firstSIMNew[ApplicationConstants.KEY_DISPLAY_NAME] == firstSimOld[ApplicationConstants.KEY_DISPLAY_NAME] &&
                firstSIMNew[ApplicationConstants.KEY_MOBILE_NUMBER] == firstSimOld[ApplicationConstants.KEY_MOBILE_NUMBER]) -> {
            ApplicationConstants.NO_CHANGE
        }
        (firstSIMNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == secondSIMOld[ApplicationConstants.KEY_SUBSCRIPTION_ID] &&
                firstSIMNew[ApplicationConstants.KEY_DISPLAY_NAME] == secondSIMOld[ApplicationConstants.KEY_DISPLAY_NAME] &&
                firstSIMNew[ApplicationConstants.KEY_MOBILE_NUMBER] == secondSIMOld[ApplicationConstants.KEY_MOBILE_NUMBER]) -> {
            ApplicationConstants.SIM_TWO_POSITION_CHANGE
        }
        (firstSIMNew[ApplicationConstants.KEY_SUBSCRIPTION_ID] == ApplicationConstants.NOT_SET) -> {
            ApplicationConstants.SLOT_EMPTY
        }
        else -> {
            ApplicationConstants.NEW_SIM_INSERTED
        }
    }
    override fun onCleared() {
        super.onCleared()
    }
}