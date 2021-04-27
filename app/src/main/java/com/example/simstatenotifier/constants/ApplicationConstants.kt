package com.example.simstatenotifier

class ApplicationConstants {

    companion object {

        const val NOT_SET = "NOT SET"
        const val SLOT_EMPTY = "EMPTY"
        const val NO_CHANGE = "NO CHANGE"
        const val SIM_SWAPPED = "SIM swapped"
        const val SIM_ONE_POSITION_CHANGE = "SIM 1 is shifted from Slot 1 to slot 2"
        const val SIM_TWO_POSITION_CHANGE = "SIM 2 is shifted from Slot 2 to slot 1"
        const val NEW_SIM_INSERTED = "New SIM inserted"

        //Shared Preference Keys
        const val SHARED_PREF_SIM_DATA = "sim_data"
        const val KEY_SLOT_FIRST = "slot_1"
        const val KEY_SLOT_SECOND = "slot_2"
        const val KEY_SLOT_FIRST_OLD = "slot_1_old"
        const val KEY_SLOT_SECOND_OLD = "slot_2_old"
        const val KEY_DISPLAY_NAME = "display_name"
        const val KEY_SUBSCRIPTION_ID = "subscription_id"
        const val KEY_MOBILE_NUMBER = "mobile_no"
    }

}