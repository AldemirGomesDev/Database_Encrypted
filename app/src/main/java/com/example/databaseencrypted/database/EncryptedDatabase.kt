package com.example.databaseencrypted.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.databaseencrypted.database.dao.UserDao
import com.example.databaseencrypted.database.model.User
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(
    entities = arrayOf(User::class),
    version = 1
)
abstract class EncryptedDatabase : RoomDatabase() {

    abstract fun UserDao(): UserDao

    companion object {
        fun getInstance(passcode: CharArray, context: Context):
                EncryptedDatabase = buildDatabase(passcode, context)

        private fun buildDatabase(
            passcode: CharArray,
            context: Context
        ): EncryptedDatabase {
            val dbKey = EncryptionSetting(context).getCharKey(passcode)
            val supportFactory = SupportFactory(SQLiteDatabase.getBytes(dbKey))
            return Room.databaseBuilder(context, EncryptedDatabase::class.java,
                "encrypted-db").openHelperFactory(supportFactory).build()
        }
    }
}