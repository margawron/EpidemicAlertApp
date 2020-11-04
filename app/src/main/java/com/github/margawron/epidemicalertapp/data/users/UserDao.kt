package com.github.margawron.epidemicalertapp.data.users

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE login like :login")
    fun findByLogin(login: String): LiveData<User>
}