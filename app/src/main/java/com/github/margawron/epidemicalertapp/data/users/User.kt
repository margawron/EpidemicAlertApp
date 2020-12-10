package com.github.margawron.epidemicalertapp.data.users

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
@TypeConverters(
    value = [
        RoleConverter::class,
        AccountStateConverter::class
    ]
)
class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "role")
    val role: Role,

    @ColumnInfo(name = "account_state")
    val accountState: AccountState
)