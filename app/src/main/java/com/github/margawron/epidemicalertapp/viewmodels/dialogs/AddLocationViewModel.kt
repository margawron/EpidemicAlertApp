package com.github.margawron.epidemicalertapp.viewmodels.dialogs

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.api.location.LocationType
import com.github.margawron.epidemicalertapp.dialogs.AddLocationDialog
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class AddLocationViewModel(
    private val onAddLocationResult: OnAddLocationResult
) : ViewModel() {

    lateinit var context: Context
    lateinit var dialog: AddLocationDialog

    val items by lazy {
        LocationType.values().map {
            context.getString(it.stringId)
        }.toMutableList()
    }
    var description = ""
    var day: Int? = null
    var month: Int? = null // need to add 1 to get true value
    var year: Int? = null
    var locationTypeOrdinal: Int = 0

    fun onAccept() {
        if (day == null || month == null || year == null) {
            Toast.makeText(
                context,
                context.getString(R.string.expiry_date_not_selected),
                Toast.LENGTH_SHORT
            ).show()
        } else if (description.isEmpty()) {
            Toast.makeText(
                context,
                context.getString(R.string.description_not_set),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            onAddLocationResult.onAccept(
                description,
                LocalDate.of(year!!, month!! + 1, day!!)
                    .atStartOfDay()
                    .toInstant(
                        ZoneOffset.UTC
                    ),
                LocationType.values()[locationTypeOrdinal]
            )
            dialog.dismiss()
        }
    }

    fun onCancel() {
        dialog.dismiss()
    }

    fun interface OnAddLocationResult {
        fun onAccept(description: String, time: Instant, locationType: LocationType)
    }
}