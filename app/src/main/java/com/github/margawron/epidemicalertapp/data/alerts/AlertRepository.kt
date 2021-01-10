package com.github.margawron.epidemicalertapp.data.alerts

import android.util.Log
import androidx.lifecycle.LiveData
import com.github.margawron.epidemicalertapp.api.alerts.AlertDto
import com.github.margawron.epidemicalertapp.api.alerts.AlertService
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.data.pathogens.PathogenRepository
import com.github.margawron.epidemicalertapp.data.users.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertRepository @Inject constructor(
    private val alertDao: AlertDao,
    private val alertService: AlertService,
    private val authManager: AuthManager,
    private val pathogenRepository: PathogenRepository,
    private val userRepository: UserRepository,
)  {

    fun getAlertsLiveData(): LiveData<List<Alert>> {
        val user = authManager.getLoggedInUser()
        return alertDao.getAllLiveData(user.id)
    }

    suspend fun getAlertData(alertId: Long): ApiResponse<AlertDto> {
        val response = alertService.getSingleAlert(alertId)
        if(response is ApiResponse.Success){
            val body = response.body!!
            updateAlert(body)
        }
        return response
    }

    suspend fun refreshAlerts(){
        when(val response = alertService.getAllOwnAlerts()){
            is ApiResponse.Success -> {
                val alertList = response.body!!
                Log.e("OE", "alertList size ${alertList.size}")
                for(dto in alertList){
                    val dbAlert = alertDao.findById(dto.id)
                    if(dbAlert == null){
                        insertAlert(dto)
                    } else {
                        updateAlert(dto)
                    }
                }
            }
            is ApiResponse.Error -> {
                val error = ApiResponse.errorToMessage(response)
                Log.e("OE", error.toString() ?: "no error")
            }
        }
    }

    private suspend fun updateAlert(dto: AlertDto) {
        val dbAlert = alertDao.findById(dto.id)!!
        val dbPathogen = pathogenRepository.fetchPathogenById(dto.pathogenId) ?: return
        pathogenRepository.updatePathogen(dbPathogen)
        with(dbAlert){
            pathogenId = dto.pathogenId
            suspectId = dto.suspectId
            victimId = dto.victimId
            suspicionLevel = dto.suspicionLevel
        }
        alertDao.update(dbAlert)
    }

    private suspend fun insertAlert(dto: AlertDto){
        val dbPathogen = pathogenRepository.fetchPathogenById(dto.pathogenId) ?: return
        var dbVictim = userRepository.getUserById(dto.victimId)
        if(dbVictim == null){
            dbVictim = userRepository.createAnonymousUserForId(dto.victimId)
        }
        var dbSuspect = userRepository.getUserById(dto.suspectId)
        if(dbSuspect == null){
            dbSuspect = userRepository.createAnonymousUserForId(dto.suspectId)
        }
        val alert = Alert(
            dto.id,
            dto.victimId,
            dto.suspectId,
            dbPathogen.id,
            dto.suspicionLevel,
            dto.alertCreationInstant
        )
        alertDao.insert(alert)
    }
}