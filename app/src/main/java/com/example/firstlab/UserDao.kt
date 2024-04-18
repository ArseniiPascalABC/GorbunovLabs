package com.example.firstlab

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    fun insert(user: User): Long

    @Query("SELECT * FROM user WHERE user_name = :userName")
    fun getUser(userName: String): User?
}