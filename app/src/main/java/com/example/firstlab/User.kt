package com.example.firstlab

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) var userId: Long = 0L,
    @ColumnInfo(name = "user_name")
    var userName: String,
    @ColumnInfo(name = "user_password")
    var userPassword: String
)
