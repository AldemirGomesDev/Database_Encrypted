package com.example.databaseencrypted

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.databaseencrypted.database.EncryptedDatabase
import com.example.databaseencrypted.database.dao.UserDao
import com.example.databaseencrypted.database.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val TAG = "EncryptionDatabase"
    private val passcode = "0123456789ABCDEF".toCharArray()
    lateinit var database: EncryptedDatabase
    lateinit var userDao: UserDao


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                val delay: Long = 5000
                Log.d(TAG, "passcode: $passcode")
                database = EncryptedDatabase.getInstance(passcode, requireContext())
                userDao = database.UserDao()
                GlobalScope.launch {
                val user = userDao.getAll()
                Log.d(TAG, "User: $user")
                delay(delay)

                val user2 = userDao.getAll()
                Log.d(TAG, "User2: $user2")
                delay(delay)

                val user3 = userDao.getAll()
                Log.d(TAG, "User2: $user3")
                delay(delay)


                val user4 = userDao.getAll()
                Log.d(TAG, "User4: $user4")
                delay(delay)


                val user5 = userDao.getAll()
                Log.d(TAG, "User5: $user5")

            }
        }
    }
}