package com.github.margawron.epidemicalertapp.databinds.viewmodels.fragment

import android.content.Context
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.auth.AuthManager
import com.github.margawron.epidemicalertapp.data.alerts.Alert
import com.github.margawron.epidemicalertapp.data.alerts.AlertRepository
import com.github.margawron.epidemicalertapp.data.pathogens.PathogenRepository
import com.github.margawron.epidemicalertapp.databinding.AlertFragmentBinding
import com.github.margawron.epidemicalertapp.databinds.adapters.AlertItemAdapter
import com.github.margawron.epidemicalertapp.databinds.viewmodels.adapter.AlertViewModel
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

    private fun onAlertClick() = AlertViewModel.AlertClickListener {
        Toast.makeText(context, "pressed alert with id: $it", Toast.LENGTH_SHORT).show()
    }
}