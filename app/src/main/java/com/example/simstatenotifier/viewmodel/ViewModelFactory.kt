package com.example.simstatenotifier.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.simstatenotifier.helper.SetSimDataHelper
import com.example.simstatenotifier.helper.SimDataHelper
import com.example.simstatenotifier.sharedpreferences.SharedPreferencesForData

class ViewModelFactory(
    private val simDataHelper: SimDataHelper
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                simDataHelper
            ) as T
        }
        throw IllegalArgumentException("Unknown class")
    }
}