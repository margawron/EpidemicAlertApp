package com.github.margawron.epidemicalertapp.data.users

import com.github.margawron.epidemicalertapp.api.users.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(
    userDao: UserDao,
    userService: UserService
) {
}