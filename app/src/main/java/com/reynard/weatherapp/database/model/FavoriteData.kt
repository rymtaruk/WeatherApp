package com.reynard.weatherapp.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorite_data")
data class FavoriteData (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @SerializedName("latitude")
    @ColumnInfo(name = "latitude")
    var latitude: Double = 0.0,
    @SerializedName("longitude")
    @ColumnInfo(name = "longitude")
    var longitude: Double = 0.0,
)