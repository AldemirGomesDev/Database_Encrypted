package com.example.databaseencrypted

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.databaseencrypted.database.EncryptedDatabase
import java.io.IOException

open class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        applicationContextApp = applicationContext
    }

    companion object {
        var database: EncryptedDatabase? = null
        lateinit var applicationContextApp : Context
    }

    fun getDb(): EncryptedDatabase {
        return Room.databaseBuilder(
            applicationContextApp,
            EncryptedDatabase::class.java,
            "pdo"

        ).build()
    }

    @Throws(IOException::class)
    fun closeDb() {
        getDb().openHelper.close()
    }
}