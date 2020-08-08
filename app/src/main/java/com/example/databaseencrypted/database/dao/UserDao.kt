package com.example.databaseencrypted.database.dao

import androidx.room.*
import com.example.databaseencrypted.database.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE userName LIKE :userName LIMIT 1")
    fun findByUserName(userName: String): User

    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1")
    fun findByUserId(userId: Long): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg user: User)

    @Delete
    fun delete(user: User)

    @Update
    fun update(vararg user: User)
}