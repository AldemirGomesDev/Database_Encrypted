package com.example.databaseencrypted.database

import android.content.SharedPreferences

class AppPreference (private val prefs: SharedPreferences){

    companion object {
        const val KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
    }


    fun setMasterKey(key: String){
        prefs.edit().putString(KEY_ACCESS_TOKEN,key).apply()
    }

    fun getMasterKey(): String? = prefs.getString(KEY_ACCESS_TOKEN,"")


}