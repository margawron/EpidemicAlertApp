package com.github.margawron.epidemicalertapp.data.users

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.github.margawron.epidemicalertapp.api.users.UserDto

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
){
    companion object {
        fun fromUserDto(userDto: UserDto): User{
            val (id, username, useremail, role, accountCreationDate, accountExpirationDate, accountState) = userDto
            return User(id!!, username, role, accountState)
        }
    }
}