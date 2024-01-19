package com.reynard.weatherapp.model.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Reynard on January, 20 2024
 **
 * @author BCA Digital
 **/
data class FourCastData (
    val dt: Long?,
    val main: MainData?,
    val weather: List<WeatherData?>,
    val clouds: CloudsData?,
    val wind: WindData?,
    val visibility: Int?,
    val pop: Double?,
    val sys: SysData?,
    @SerializedName("dt_txt")
    val dtTxt: String?
)