package com.github.margawron.epidemicalertapp.data.alerts

import androidx.room.*
import com.github.margawron.epidemicalertapp.data.pathogens.Pathogen
import com.github.margawron.epidemicalertapp.data.users.User

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
    SuspicionLevelConverter::class
])
class Alert(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "suspect_id")
    val suspectId: Int,

    @ColumnInfo(name = "pathogen_id")
    val pathogenId: Int,

    @ColumnInfo(name = "suspicion_level")
    val suspicionLevel: SuspicionLevel
)