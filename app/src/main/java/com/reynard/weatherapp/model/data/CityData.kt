package com.reynard.weatherapp.model.data

/**
 * Created by Reynard on January, 20 2024
 **
 * @author BCA Digital
 **/
data class CityData(
    val id: Int,
    val name: String,
    val coord: CoordinateData,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)