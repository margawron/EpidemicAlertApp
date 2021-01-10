package com.github.margawron.epidemicalertapp.databinds.viewmodels.adapter

import android.content.Context
import androidx.lifecycle.ViewModel
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.data.alerts.Alert
import com.github.margawron.epidemicalertapp.data.pathogens.Pathogen
import com.github.margawron.epidemicalertapp.data.users.User
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class AlertViewModel(
    private val alert: Alert,
    private val pathogen: Pathogen,
    private val alertClickListener: AlertClickListener,
    private val context: Context,
) : ViewModel() {

    fun getAlertCreationDate() = "${context.getString(R.string.display_alert_creation_date)} ${
        alert.alertCreationDate.atZone(
            ZoneId.systemDefault())
            .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
    }"

    fun getAlertPathogen() = "${context.getString(R.string.display_pathogen_name)} ${pathogen.name}"

    fun getSuspicionLevel() = "${context.getString(R.string.display_suspicion_level)} ${context.getString(alert.suspicionLevel.stringId)}"

    fun getAlertId() = "${context.getString(R.string.display_alert_id)} ${alert.id}"

    fun onAlertClick() {
        alertClickListener.onAlertClick(alert.id)
    }

    fun interface AlertClickListener {
        fun onAlertClick(alertId: Long)
    }
}