package com.example.firstlab

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {


    suspend fun insert(user: User){
        withContext(Dispatchers.IO){
            userDao.insert(user)
        }
    }

    suspend fun getUser(userName: String): User?{
        return withContext(Dispatchers.IO) {
            userDao.getUser(userName)
        }
    }
}