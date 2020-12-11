package com.github.margawron.epidemicalertapp.data.poi

import androidx.room.*

@Entity(tableName = "poi_location")
@TypeConverters(value = [
    PoiTypeConverter::class
])
class PoiLocation(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "latitude")
    val latitude: Float,

    @ColumnInfo(name = "longitude")
    val longitude: Float,

    @ColumnInfo(name = "poi_type")
    val locationType: PoiType
)