package com.github.margawron.epidemicalertapp.data.pathogens

import androidx.lifecycle.LiveData
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.api.pathogens.PathogenService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PathogenRepository @Inject constructor(
    private val pathogenDao: PathogenDao,
    private val pathogenService: PathogenService,
) {

    fun getPathogensLiveData(): LiveData<List<Pathogen>> {
        return pathogenDao.getAllPathogens()
    }

    suspend fun refreshPathogenDb(): ApiResponse.Error? {
        return when(val response = pathogenService.getAllPathogens()){
            is ApiResponse.Success -> {
                val body = response.body!!
                createOrUpdatePathogens(body)
                null
            }
            is ApiResponse.Error -> {
                return response
            }
        }
    }

    private suspend fun createOrUpdatePathogens(pathogenList: List<Pathogen>) {
        for (pathogen in pathogenList) {
            if (pathogenDao.findById(pathogen.id) == null) {
                pathogenDao.insert(pathogen)
            } else {
                pathogenDao.update(pathogen)
            }
        }
    }

}