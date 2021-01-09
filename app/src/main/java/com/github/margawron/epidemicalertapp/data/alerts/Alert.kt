package com.github.margawron.epidemicalertapp.data.alerts

import androidx.room.*
import com.github.margawron.epidemicalertapp.data.measurments.InstantConverter
import com.github.margawron.epidemicalertapp.data.pathogens.Pathogen
import com.github.margawron.epidemicalertapp.data.users.User
import java.time.Instant

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            childColumns = ["user_id"],
            parentColumns = ["id"]
        ),
        ForeignKey(
            entity = User::class,
            childColumns = ["suspect_id"],
            parentColumns = ["id"]
        ),
        ForeignKey(
            entity = Pathogen::class,
            childColumns = ["pathogen_id"],
            parentColumns = ["id"]
        )
    ],
    indices = [
        Index(value = ["user_id"], name = "AlertUserIndex"),
        Index(value = ["suspect_id"], name = "AlertSuspectIndex"),
        Index(value = ["pathogen_id"], name = "AlertPathogenIndex")
    ]
)
@TypeConverters(value = [
    SuspicionLevelConverter::class,
    InstantConverter::class,
])
class Alert(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "user_id")
    var victimId: Long,

    @ColumnInfo(name = "suspect_id")
    var suspectId: Long,

    @ColumnInfo(name = "pathogen_id")
    var pathogenId: Long,

    @ColumnInfo(name = "suspicion_level")
    var suspicionLevel: SuspicionLevel,

    @ColumnInfo(name = "suspect_start_time")
    var suspectStartTime: Instant
)