package com.github.margawron.epidemicalertapp.data.measurments

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.github.margawron.epidemicalertapp.data.users.User
import java.time.ZonedDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"]
        )
    ]
)
class Measurement(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    val id: Int,

    @ColumnInfo(name = "server_side_id")
    val serverId: Int,

    @ColumnInfo(name = "ownerId")
    val userId: Int,

    @ColumnInfo(name = "measurement_time")
    val measurementTime: ZonedDateTime,

    @ColumnInfo(name = "latitude")
    val latitude: Float,

    @ColumnInfo(name = "longitude")
    val longitude: Float,

    @ColumnInfo(name = "accuracy")
    val accuracy: Float,

    @ColumnInfo(name = "bearing")
    val bearing: Float,

    @ColumnInfo(name = "bearing_accuracy")
    val bearingAccuracy: Float
)