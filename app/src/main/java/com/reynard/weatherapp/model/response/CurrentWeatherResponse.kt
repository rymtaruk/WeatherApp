package com.reynard.weatherapp.model.response

import com.reynard.weatherapp.model.data.CloudsData
import com.reynard.weatherapp.model.data.CoordinateData
import com.reynard.weatherapp.model.data.MainData
import com.reynard.weatherapp.model.data.SysData
import com.reynard.weatherapp.model.data.WeatherData
import com.reynard.weatherapp.model.data.WindData

/**
 * Created by Reynard on January, 20 2024
 **
 * @author BCA Digital
 **/
data class CurrentWeatherResponse(
    val coord: CoordinateData,
    val weather: List<WeatherData>,
    val base: String,
    val main: MainData,
    val visibility: Int,
    val wind: WindData,
    val clouds: CloudsData,
    val dt: Long,
    val sys: SysData,
    val timezone: Int,
    val id: Int,
    var name: String,
    val cod: Int
)