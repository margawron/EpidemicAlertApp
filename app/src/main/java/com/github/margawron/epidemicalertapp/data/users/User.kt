package com.github.margawron.epidemicalertapp.data.users

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "bearer_token")
    var token: String,

    @ColumnInfo(name = "bearer_token_expiry_time")
    var tokenExpiryDate: Long,

    @ColumnInfo(name = "remember_password")
    val remember_password: Boolean
)