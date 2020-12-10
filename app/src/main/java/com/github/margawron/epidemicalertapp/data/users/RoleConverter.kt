package com.github.margawron.epidemicalertapp.data.users

import androidx.room.TypeConverter

class RoleConverter {

    @TypeConverter
    fun fromRole(role: Role): String {
        return when (role) {
            Role.USER -> "U"
            Role.MODERATOR -> "M"
            Role.ADMINISTRATOR -> "A"
        }
    }

    @TypeConverter
    fun fromString(string: String): Role {
        return when (string) {
            "U" -> Role.USER
            "M" -> Role.MODERATOR
            "A" -> Role.ADMINISTRATOR
            else -> throw IllegalStateException("Invalid role code")
        }
    }
}