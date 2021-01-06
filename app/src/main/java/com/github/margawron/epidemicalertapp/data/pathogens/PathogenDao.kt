package com.github.margawron.epidemicalertapp.data.pathogens

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PathogenDao {

    @Insert
    suspend fun insert(pathogen: Pathogen)

    @Update
    suspend fun update(pathogen: Pathogen)

    @Query("SELECT * FROM Pathogen WHERE id = :id")
    suspend fun findById(id: Long): Pathogen?

    @Query("SELECT * FROM Pathogen")
    fun getAllPathogens(): LiveData<List<Pathogen>>

}