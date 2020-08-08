package com.example.databaseencrypted

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.databaseencrypted.database.EncryptedDatabase
import com.example.databaseencrypted.database.EncryptionSetting
import com.example.databaseencrypted.database.dao.UserDao
import com.example.databaseencrypted.database.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val TAG = "EncryptionDatabase"
    private val passcode = "0123456789ABCDEF".toCharArray()
    lateinit var database: EncryptedDatabase
    lateinit var userDao: UserDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->

            createDataBase()

            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

    }

    private fun createDataBase() {
        database = EncryptedDatabase.getInstance(passcode, applicationContext)
        userDao = database.UserDao()
        GlobalScope.launch {
            userDao.insert(User(2, "jose2", "jose@gmail.com", "comum"))
            userDao.insert(User(3, "jose3", "jose@gmail.com", "comum"))
            userDao.insert(User(4, "jose4", "jose@gmail.com", "comum"))
            userDao.insert(User(5, "jose5", "jose@gmail.com", "comum"))
            userDao.insert(User(6, "jose6", "jose@gmail.com", "comum"))
//            val user = userDao.getAll()
//            println("Hello: $user")
//            Log.d(TAG, "User: $user")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}