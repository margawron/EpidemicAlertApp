package com.github.margawron.epidemicalertapp.data.users

import com.github.margawron.epidemicalertapp.R

enum class Role(val stringId: Int) {
    USER(R.string.role_user),
    MODERATOR(R.string.role_mod),
    ADMINISTRATOR(R.string.role_admin);
}