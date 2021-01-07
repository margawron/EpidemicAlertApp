package com.github.margawron.epidemicalertapp.data.measurments

import androidx.room.*
import com.github.margawron.epidemicalertapp.data.users.User
import java.time.Instant

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["owner_id"]
        )
    ],
    indices = [
        Index(value = ["server_side_id"], name = "MeasurementServerSideIdIndex", unique = true),
        Index(value = ["sent_to_server"], name = "MeasurementWasSentToServerIndex"),
        Index(value = ["measurement_time"], name = "MeasurementTakenTimeIndex"),
        Index(value = ["owner_id"], name = "MeasurementOwnerIndex")
    ]
)
@TypeConverters(value = [
    InstantConverter::class
])
class Measurement(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "local_id")
    val id: Long?,

    @ColumnInfo(name = "server_side_id")
    var serverId: Long?,

    @ColumnInfo(name = "owner_id")
    val userId: Long,

    @ColumnInfo(name = "measurement_time")
    val measurementTime: Instant,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "accuracy")
    val accuracy: Float,

    @ColumnInfo(name = "bearing")
    val bearing: Float,

    @ColumnInfo(name = "bearing_accuracy")
    val bearingAccuracy: Float,

    @ColumnInfo(name = "sent_to_server")
    var wasSentToServer: Boolean = false
){
    companion object{
    }
}