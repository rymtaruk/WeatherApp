package com.reynard.weatherapp.model.response

import com.reynard.weatherapp.model.data.CityData
import com.reynard.weatherapp.model.data.FourCastData

/**
 * Created by Reynard on January, 20 2024
 * *
 *
 * @author BCA Digital
 */
data class WeatherResponse(
    val cod: String?,
    val message: Int?,
    val cnt: Int?,
    val list: List<FourCastData?>,
    val city: CityData?
)