package com.example.simstatenotifier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(
    private val sharedPreferencesForData: SharedPreferencesForData,
    private val simDataHelper: SimDataHelper
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(sharedPreferencesForData, simDataHelper) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}