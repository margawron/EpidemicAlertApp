package com.github.margawron.epidemicalertapp.data.users

import androidx.room.TypeConverter
import java.lang.IllegalStateException

class AccountStateConverter {

    @TypeConverter
    fun fromEnum(accountState: AccountState): String {
        return when (accountState) {
            AccountState.NORMAL -> "N"
            AccountState.BANNED -> "B"
        }
    }

    @TypeConverter
    fun fromString(string: String): AccountState {
        return when (string) {
            "N" -> AccountState.NORMAL
            "B" -> AccountState.BANNED
            else -> throw IllegalStateException("Invalid account state mapping string")
        }
    }
}