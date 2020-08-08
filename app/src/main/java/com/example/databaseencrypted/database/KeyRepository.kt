package com.example.databaseencrypted.database

import com.example.databaseencrypted.database.AppPreference

class KeyRepository(private val appPreference: AppPreference) {

    fun setMasterKey(key: String){
        appPreference.setMasterKey(key)
    }

    fun getMasterKey(): String? = appPreference.getMasterKey()
}