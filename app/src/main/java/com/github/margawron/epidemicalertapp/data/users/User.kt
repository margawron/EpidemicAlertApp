package com.github.margawron.epidemicalertapp.data.users

import androidx.room.*

@Entity
@TypeConverters(value = [
    RoleConverter::class,
    AccountStateConverter::class
])
class User(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "role")
    val role: Role,

    @ColumnInfo(name = "account_state")
    val accountState: AccountState
)