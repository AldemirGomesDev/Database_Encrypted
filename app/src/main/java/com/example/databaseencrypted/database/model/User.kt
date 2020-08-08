package com.example.databaseencrypted.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "userName") val userName: String,
    @ColumnInfo(name = "userEmail") val userEmail: String,
    @ColumnInfo(name = "userType") val userType: String
)