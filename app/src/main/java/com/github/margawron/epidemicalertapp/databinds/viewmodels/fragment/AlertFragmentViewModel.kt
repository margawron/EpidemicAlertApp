package com.github.margawron.epidemicalertapp.databinds.viewmodels.fragment

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.data.alerts.Alert
import com.github.margawron.epidemicalertapp.data.alerts.AlertRepository
import com.github.margawron.epidemicalertapp.data.pathogens.PathogenRepository
import com.github.margawron.epidemicalertapp.databinding.AlertFragmentBinding
import com.github.margawron.epidemicalertapp.databinds.adapters.AlertItemAdapter
import com.github.margawron.epidemicalertapp.databinds.viewmodels.adapter.AlertViewModel
import com.github.margawron.epidemicalertapp.dialogs.AlertDetailsDialog
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertFragmentViewModel @ViewModelInject internal constructor(
    private val authManager: AuthManager,
    private val alertRepository: AlertRepository,
    private val pathogenRepository: PathogenRepository,
    @ActivityContext private val context: Context,
): ViewModel() {

    lateinit var binding: AlertFragmentBinding
    lateinit var fragment: Fragment

    fun onInit() {
        viewModelScope.launch {
            setupAlertLiveData()
            refreshAlerts()
        }
    }

    private suspend fun setupAlertLiveData() {
        val alertsLiveData = alertRepository.getAlertsLiveData()
        val observer = Observer<List<Alert>> {
            CoroutineScope(Dispatchers.IO).launch {
                val alertViewModels = it.map {
                    AlertViewModel(
                        it,
                        pathogenRepository.fetchPathogenById(it.pathogenId)!!,
                        onAlertClick(),
                        context
                    )
                }.toList()
                withContext(Dispatchers.Main) {
                    binding.alertRecycler.adapter = AlertItemAdapter(alertViewModels)
                }
            }
        }
        withContext(Dispatchers.Main) {
            alertsLiveData.observe(binding.lifecycleOwner!!, observer)
        }
    }

    private suspend fun refreshAlerts() {
        withContext(Dispatchers.IO) {
            alertRepository.refreshAlerts()
        }
    }

    private fun onAlertClick() = AlertViewModel.AlertClickListener { alertId ->
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                showDetailsOfAlert(alertId)
            }
        }
    }

    private suspend fun showDetailsOfAlert(alertId: Long) {
        val response = alertRepository.getAlertData(alertId)
        withContext(Dispatchers.Main){
            when(response){
                is ApiResponse.Success -> {
                    AlertDetailsDialog(response.body!!)
                        .show(fragment.parentFragmentManager, AlertDetailsDialog.DIALOG_TAG)
                }
                is ApiResponse.Error -> {
                    val errors = ApiResponse.errorToMessage(response)
                    Toast.makeText(context, errors, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}