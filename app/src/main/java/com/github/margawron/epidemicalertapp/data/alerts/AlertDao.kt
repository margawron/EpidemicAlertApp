package com.github.margawron.epidemicalertapp.data.alerts

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlertDao {

    @Insert
    fun insert(alert: Alert)

    @Update
    fun update(alert: Alert)

    @Query("SELECT * FROM Alert WHERE id = :id")
    fun findById(id: Long): Alert?

    @Query("SELECT * FROM Alert WHERE user_id = :userId OR suspect_id = :userId")
    fun getAllLiveData(userId: Long): LiveData<List<Alert>>
}