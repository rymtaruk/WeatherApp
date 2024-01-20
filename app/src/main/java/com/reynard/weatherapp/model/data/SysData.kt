package com.reynard.weatherapp.model.data

/**
 * Created by Reynard on January, 20 2024
 **
 * @author BCA Digital
 **/
data class SysData(
    val type: Int? = 0,
    val id: Int? = 0,
    val country: String? = null,
    val sunrise: Long? = 0,
    val sunset: Long? = 0,
    val pod: String? = null
)