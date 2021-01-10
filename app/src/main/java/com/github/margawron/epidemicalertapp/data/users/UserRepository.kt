package com.github.margawron.epidemicalertapp.data.users

import com.github.margawron.epidemicalertapp.api.common.ApiResponse
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

    fun updateFetchedUser(userDto: UserDto): User {
        val existingUser = userDao.findById(userDto.id!!)
        if(existingUser == null){
            userDao.insert(User.fromUserDto(userDto))
        } else {
            userDao.update(User.fromUserDto(userDto))
        }
        return userDao.findById(userDto.id!!)!!
    }

    fun getUserById(userId: Long): User? {
        return userDao.findById(userId)
    }

    fun createAnonymousUserForId(userId: Long): User {
        val anonymousUser = User(
            userId,
            "Anonymous",
            Role.USER,
            AccountState.NORMAL
        )
        userDao.insert(anonymousUser)
        return userDao.findById(userId)!!
    }

    suspend fun findUserByName(username: String) = userService.getUserWithNameLike(username)

    suspend fun changeUserRole(userId: Long, role: Role): ApiResponse<UserDto> = userService.changeUserRole(userId, role)
}