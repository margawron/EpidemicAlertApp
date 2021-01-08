package com.github.margawron.epidemicalertapp.databinds.viewmodels.dialogs

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.api.users.UserDto
import com.github.margawron.epidemicalertapp.data.alerts.SuspicionLevel
import com.github.margawron.epidemicalertapp.data.pathogens.Pathogen
import com.github.margawron.epidemicalertapp.data.pathogens.PathogenRepository
import com.github.margawron.epidemicalertapp.databinding.MarkSuspectDialogBinding
import com.github.margawron.epidemicalertapp.dialogs.MarkSuspectDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MarkSuspectViewModel internal constructor(
    private val pathogenRepository: PathogenRepository,
    private val userDto: UserDto,
    private val onMarkSuspect: OnMarkSuspect,
) : ViewModel() {

    lateinit var binding: MarkSuspectDialogBinding
    lateinit var context: Context
    lateinit var dialog: MarkSuspectDialog

    private val pathogenEntriesLiveData = pathogenRepository.getPathogensLiveData()
    private lateinit var pathogenNameToIdMap: Map<String, Pathogen>
    private lateinit var pathogenNames: Array<String>

    fun setup() {
        viewModelScope.launch {
            setupPathogenSpinner()
        }
        setupDateLiveData()
    }

    private suspend fun setupPathogenSpinner() {
        withContext(Dispatchers.IO) {
            val error = pathogenRepository.refreshPathogenDb()
            if (error != null) {
                val errorString = ApiResponse.errorToMessage(error)
                withContext(Dispatchers.Main){
                    Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
                }
            }
        }
        val pathogenObserver = Observer<List<Pathogen>> { pathogens ->
            pathogenNameToIdMap = pathogens.map { it.name to it }.toMap()
            pathogenNames = pathogenNameToIdMap.keys.toTypedArray()
            binding.markPathogenSpinner.adapter = ArrayAdapter(
                context,
                android.R.layout.simple_list_item_1,
                pathogenNames
            )
        }
        pathogenEntriesLiveData.observe(dialog, pathogenObserver)
    }

    var suspicionLevelOrdinal = 0
    val suspicionLevels by lazy {
        SuspicionLevel.values().map {
            context.getString(it.stringId)
        }.toList()
    }

    private var selectedDateOfInfectionLiveData: MutableLiveData<Instant> =
        MutableLiveData(Instant.now())
    var selectedDateOfInfection = MutableLiveData("")
    private fun setupDateLiveData() {
        selectedDateOfInfectionLiveData.observeForever {
            val date = it.atZone(ZoneId.systemDefault()).toLocalDate()
            val formattedDate = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
            selectedDateOfInfection.value =
                "${context.getString(R.string.set_date_of_infection)}\n${formattedDate}"
        }
    }

    fun getUsername() = "${context.getString(R.string.username)} ${userDto.username}"

    var pathogenEntryNumber = 0

    fun selectDateOfInfection() {
        val picker = DatePickerDialog(context)
        picker.setOnDateSetListener { _, year, month, dayOfMonth ->
            selectedDateOfInfectionLiveData.value = LocalDate.of(year, month + 1, dayOfMonth)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
        }
        picker.show()
    }

    fun markUser() {
        val pathogenKey = pathogenNames[pathogenEntryNumber]
        val pathogen = pathogenNameToIdMap[pathogenKey]
        val suspicionLevel = SuspicionLevel.values()[suspicionLevelOrdinal]
        val date = selectedDateOfInfectionLiveData.value!!
        val localDate = date.atZone(ZoneId.systemDefault()).toLocalDate()
        val localDateString = localDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
        val areYouSureAlert = AlertDialog.Builder(context)
        val message =
            "${context.getString(R.string.mark_suspect_are_you_sure)} ${userDto.username}\n${
                context.getString(R.string.mark_suspect_as_carrier)
            } ${pathogen!!.name}\n${
                context.getString(R.string.mark_suspect_from_date)
            } $localDateString\n${context.getString(R.string.mark_suspect_with_certainty)} ${
                context.getString(
                    suspicionLevel.stringId
                )
            }"
        with(areYouSureAlert) {
            setMessage(message)
            setPositiveButton(android.R.string.yes){ alertDialog, buttonId ->
                if(buttonId == DialogInterface.BUTTON_POSITIVE){
                    onMarkSuspect.onAccept(
                        selectedDateOfInfectionLiveData.value!!,
                        suspicionLevel,
                        userDto.id!!,
                        pathogen.id
                    )
                    alertDialog.dismiss()
                    dialog.dismiss()
                }
            }
            setNegativeButton(android.R.string.no){ alertDialog, buttonId ->
                if(buttonId == DialogInterface.BUTTON_NEGATIVE){
                    alertDialog.dismiss()
                }
            }
        }.show()

    }


    fun interface OnMarkSuspect {
        fun onAccept(
            startTime: Instant,
            suspicionLevel: SuspicionLevel,
            suspectId: Long,
            pathogenId: Long
        )
    }
}