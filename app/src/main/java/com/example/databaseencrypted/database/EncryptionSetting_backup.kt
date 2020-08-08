package com.example.databaseencrypted.database

import android.content.Context
import android.os.Build
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.security.AlgorithmParameters
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class EncryptionSetting_backup(private val mContext: Context) {

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

        getRawByteKey(passcode)

        return dbCharKey ?: error("Failed to decrypt database key")
    }

    private fun getRawByteKey(passcode: CharArray) {
        Log.d(TAG, "fun => getRawByteKey")
        val key = findKeyShared()

        if (key == "") {
            initKey(passcode)
        }else {
            rawByteKey = Base64.decode(key, Base64.DEFAULT)
            dbCharKey = rawByteKey.toHex().toCharArray()
        }

    }

    private fun initKey(passcode: CharArray) {
        createNewKey()
//        persistRawKey(passcode)

    }

    private fun createNewKey() {
        // This is the raw key that we'll be encrypting + storing
        Log.d(TAG, "fun => createNewKey")
        rawByteKey = generateRandomKey()
        // This is the key that will be used by Room
        dbCharKey = rawByteKey.toHex().toCharArray()

        saveKeyShared(Base64.encodeToString(rawByteKey, Base64.DEFAULT))

    }

    private fun persistRawKey(userPasscode: CharArray) {
//        Log.d(TAG, "fun => persistRawKey")
//        val storable = toStorable(rawByteKey, userPasscode)
//        // Implementation explained in next step
//        Log.d(TAG, "============saveStorable================= $storable")
//        saveToPrefs(mContext, storable)
    }
    private fun saveToPrefs(context: Context, storable: Storable) {
//        Log.d(TAG, "fun => saveToPrefs")
//        val serialized = Gson().toJson(storable)
//        val prefs = context.getSharedPreferences("database",
//            Context.MODE_PRIVATE)
//        Log.d(TAG, "key: $serialized")
//        prefs.edit().putString("key", serialized).apply()
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

    private fun toStorable(rawDbKey: ByteArray, userPasscode: CharArray): Storable {
        // Generate a random 8 byte salt
        Log.d(TAG, "fun => toStorable")
        val salt = ByteArray(8).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                SecureRandom.getInstanceStrong().nextBytes(this)
            } else {
                SecureRandom().nextBytes(this)
            }
        }

        val secret: SecretKey = generateSecretKey(userPasscode, rawDbKey)
        Log.e(TAG, "-------secret------- $secret")

        // Now encrypt the database key with PBE
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secret)
        val params: AlgorithmParameters = cipher.parameters
        val iv: ByteArray = params.getParameterSpec(IvParameterSpec::class.java).iv
        val ciphertext: ByteArray = cipher.doFinal(iv)

        // Return the IV and CipherText which can be stored to disk
        return Storable(
            Base64.encodeToString(iv, Base64.DEFAULT),
            Base64.encodeToString(ciphertext, Base64.DEFAULT),
            Base64.encodeToString(rawDbKey, Base64.DEFAULT)
        )
    }

    private fun generateSecretKey(passcode: CharArray, salt: ByteArray): SecretKey {
        // Initialize PBE with password
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec = PBEKeySpec(passcode, salt, 65536, 256)
        val tmp: SecretKey = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, "AES")
    }

    fun getStorable(context: Context): Storable? {
        Log.d(TAG, "fun => getStorable")
        val prefs = context.getSharedPreferences("database",
            Context.MODE_PRIVATE)
        val serialized = prefs.getString("key", null)
        if (serialized.isNullOrBlank()) {
            return null
        }
        return try {
            Gson().fromJson(serialized,
                object: TypeToken<Storable>() {}.type)
        } catch (ex: JsonSyntaxException) {
            null
        }
    }


    data class Storable(val iv: String, val key: String, val salt: String)
}