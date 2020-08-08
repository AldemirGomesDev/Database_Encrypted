package com.example.databaseencrypted.database

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedPrefViewModel(application: Application) : AndroidViewModel(application) {

    /*private var sharedPreferences = application.getSharedPreferences(
        "com.example.securelocker.pref", Context.MODE_PRIVATE)
     */

    private var sharedPreferences = EncryptionHelper.getSharedPref(application)
    private var appPreference = AppPreference(sharedPreferences)
    private var userRepository = KeyRepository(appPreference)

    val userNameField: MutableLiveData<String> = MutableLiveData()
    val userEmailField: MutableLiveData<String> = MutableLiveData()
    val snackBarMessage: MutableLiveData<String> = MutableLiveData()


}