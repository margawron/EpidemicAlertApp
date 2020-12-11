package com.github.margawron.epidemicalertapp.data.proximity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.github.margawron.epidemicalertapp.data.alerts.Alert
import com.github.margawron.epidemicalertapp.data.measurments.Measurement

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Measurement::class,
            parentColumns = ["id"],
            childColumns = ["measurement_id"]
        ),
        ForeignKey(
            entity = Alert::class,
            parentColumns = ["id"],
            childColumns = ["alert_id"]
        )
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
