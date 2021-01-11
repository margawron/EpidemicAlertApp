package com.github.margawron.epidemicalertapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.margawron.epidemicalertapp.R
import com.github.margawron.epidemicalertapp.api.common.ApiResponse
import com.github.margawron.epidemicalertapp.api.statistics.StatisticsDto
import com.github.margawron.epidemicalertapp.api.statistics.StatisticsService
import com.github.margawron.epidemicalertapp.databinding.StatisticsFragmentBinding
import com.github.margawron.epidemicalertapp.databinds.viewmodels.fragment.StatisticsFragmentViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.floor


@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    @Inject
    lateinit var statisticsService: StatisticsService

    private lateinit var binding: StatisticsFragmentBinding

    companion object {
        fun newInstance() = StatisticsFragment()
    }

    private val viewModel by viewModels<StatisticsFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.statistics_fragment, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = statisticsService.getStatisticsFromLastMonth()) {
                is ApiResponse.Success -> {
                    withContext(Dispatchers.Main) {
                        handleBarChart(response)
                    }
                }
                is ApiResponse.Error -> {
                    withContext(Dispatchers.Main) {
                        val errors = ApiResponse.errorToMessage(response)
                        Toast.makeText(context, errors, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return binding.root
    }

    private fun handleBarChart(response: ApiResponse.Success<Map<String, StatisticsDto>>) {
        val barChart = binding.statisticsBarchart

        val removeDecimalPlace = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                if(value < 0.5f){
                    return ""
                }
                return floor(value).toInt().toString()
            }
        }

        val statistics = response.body!!
        val (alerts, suspects) = processResponse(statistics)
        alerts.setColors(ColorTemplate.rgb("#F7CA18"))
        alerts.valueTextSize = 10.0f
        alerts.valueFormatter = removeDecimalPlace

        suspects.setColors(ColorTemplate.rgb("#FF0000"))
        suspects.valueTextSize = 10.0f
        suspects.valueFormatter = removeDecimalPlace

        val barData = BarData(alerts, suspects)
        barData.barWidth = 0.45f

        val groupSpace = 0.04f
        val barSpace = 0.02f
        val barWidth = 0.46f
        barData.barWidth = barWidth
        with(barChart.xAxis) {
            valueFormatter = IndexAxisValueFormatter(statistics.keys.toTypedArray())
            setCenterAxisLabels(true)
            axisMinimum = 0.0f
            granularity = 0.9f
            position = XAxis.XAxisPosition.BOTTOM
            labelRotationAngle = 90.0f
        }

        barChart.data = barData
        barChart.groupBars(0.0f, groupSpace, barSpace)
        barChart.description.isEnabled = false
        with(barChart.axisRight){
            axisMinimum = 0.0f
            valueFormatter = removeDecimalPlace
            this.isDrawZeroLineEnabled
        }

        with(barChart.axisLeft) {
            axisMinimum = 0.0f
            valueFormatter = removeDecimalPlace
        }
        barChart.setVisibleXRange(5.0f,7.0f)
        barChart.centerViewTo(statistics.size - 2.0f, 100000.0f, YAxis.AxisDependency.RIGHT)
        barChart.setScaleEnabled(false)
        barChart.isHighlightPerTapEnabled = false

        barChart.invalidate()
    }

    private fun processResponse(statistics: Map<String, StatisticsDto>): Pair<BarDataSet, BarDataSet> {
        val alerts = mutableListOf<BarEntry>()
        val suspects = mutableListOf<BarEntry>()
        var i = 1
        statistics.map {
            alerts.add(BarEntry(i.toFloat(), it.value.alertCount.toFloat()))
            suspects.add(BarEntry(i.toFloat(), it.value.suspectCount.toFloat()))
            ++i
        }
        return Pair(BarDataSet(alerts, "Powiadomienia"), BarDataSet(suspects, "Podejrzani"))
    }
}