package com.example.databaseencrypted.database

import android.content.Context
import android.os.Build
import android.util.Base64
import android.util.Log
import java.security.SecureRandom

class EncryptionSetting(private val mContext: Context) {

    private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
    private lateinit var rawByteKey: ByteArray

    private var sharedPreferences = EncryptionHelper.getSharedPref(mContext)
    private var appPreference = AppPreference(sharedPreferences)
    private var userRepository = KeyRepository(appPreference)

    companion object {
        private  var dbCharKey: CharArray? = null
        private const val TAG = "EncryptionDatabase"
    }

    fun getCharKey(passcode: CharArray): CharArray {
        Log.d(TAG, "fun ======> getCharKey")

        getRawByteKey()

        return dbCharKey ?: error("Failed to decrypt database key")
    }

    private fun getRawByteKey() {
        Log.d(TAG, "fun => getRawByteKey")
        val key = findKeyShared()

        if (key == "") {
            createNewKey()
        }else {
            rawByteKey = Base64.decode(key, Base64.DEFAULT)
            dbCharKey = rawByteKey.toHex().toCharArray()
        }

    }

    private fun createNewKey() {
        // This is the raw key that we'll be encrypting + storing
        Log.d(TAG, "fun => createNewKey")
        rawByteKey = generateRandomKey()
        // This is the key that will be used by Room
        dbCharKey = rawByteKey.toHex().toCharArray()

        saveKeyShared(Base64.encodeToString(rawByteKey, Base64.DEFAULT))

    }

    private fun saveKeyShared(key: String) {
        Log.d(TAG, "salvando no shared $key")
        userRepository.setMasterKey(key)
    }

    private fun findKeyShared(): String? {
        val key = userRepository.getMasterKey()
        Log.d(TAG, "chave salva no sharedPreference $key")
        return key
    }

    private fun generateRandomKey(): ByteArray =
            ByteArray(32).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    SecureRandom.getInstanceStrong().nextBytes(this)
                } else {
                    SecureRandom().nextBytes(this)
                }
            }

    private fun ByteArray.toHex(): String {
        val result = StringBuilder()
        forEach {
            val octet = it.toInt()
            val firstIndex = (octet and 0xF0).ushr(4)
            val secondIndex = octet and 0x0F
            result.append(HEX_CHARS[firstIndex])
            result.append(HEX_CHARS[secondIndex])
        }
        return result.toString()
    }

}