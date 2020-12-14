package com.github.margawron.epidemicalertapp.data.proximity

import androidx.room.*
import com.github.margawron.epidemicalertapp.data.alerts.Alert
import com.github.margawron.epidemicalertapp.data.measurments.Measurement

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Measurement::class,
            parentColumns = ["local_id"],
            childColumns = ["measurement_id"]
        ),
        ForeignKey(
            entity = Alert::class,
            parentColumns = ["id"],
            childColumns = ["alert_id"]
        )
    ],
    indices = [
        Index(value = ["measurement_id"], name = "ProximityMeasurementIndex"),
        Index(value = ["alert_id"], name = "ProximityAlertIndex")
    ]
)
class ProximityMeasurement(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "is_victim")
    val isVictim: Boolean,

    @ColumnInfo(name = "measurement_id")
    val measurementId: Int,

    @ColumnInfo(name = "alert_id")
    val alertId: Int
)
