package com.github.margawron.epidemicalertapp.data.pathogens

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
@TypeConverters(
    value = [
        PeriodResolutionConverter::class
    ]
)
class Pathogen(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "pathogen_name")
    val pathogenName: String,

    @ColumnInfo(name = "contagious_period")
    val contagiousPeriod: Int,

    @ColumnInfo(name = "period_resolution")
    val periodResolution: PeriodResolution
)