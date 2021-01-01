package com.github.margawron.epidemicalertapp.data.users

import com.github.margawron.epidemicalertapp.api.users.UserDto
import com.github.margawron.epidemicalertapp.api.users.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userService: UserService
) {
    suspend fun getSelfData() = userService.getSelf()

    fun createOrUpdateUserFrom(userDto: UserDto): User {
        val existingUser = userDao.findById(userDto.id!!)
        if(existingUser == null){
            userDao.insert(User.fromUserDto(userDto))
        } else {
            userDao.update(User.fromUserDto(userDto))
        }
        return userDao.findById(userDto.id!!)!!
    }
}