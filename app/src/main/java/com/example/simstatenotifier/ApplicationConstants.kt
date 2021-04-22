package com.example.simstatenotifier

class ApplicationConstants {

    companion object {
        const val SIM_STATE_ABSENT = "ABSENT"
        const val SIM_STATE_NETWORK_LOCKED = "NETWORK LOCKED"
        const val SIM_STATE_PIN_REQUIRED = "PIN REQUIRED"
        const val SIM_STATE_PUK_REQUIRED = "PUK REQUIRED"
        const val SIM_STATE_UNKNOWN = "UNKNOWN"
        const val SIM_STATE_READY = "READY!!"

        const val NOT_SET = "NOT SET"
        const val SIM_STATE = "SIM STATE: "
        const val NETWORK_PROVIDER = "NETWORK PROVIDER: "
        const val MOBILE = "MOBILE NO: "

        //Shared Preference Keys
        const val SHARED_PREF_SIM_DATA = "sim_data"
        const val KEY_SIM_STATUS = "sim_status"
        const val KEY_NETWORK_PROVIDER = "network_provider"
        const val KEY_MOBILE_NO = "mobile_no"
    }

}