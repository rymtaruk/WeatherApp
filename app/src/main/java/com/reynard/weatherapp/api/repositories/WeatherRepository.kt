package com.reynard.weatherapp.api.repositories

import com.reynard.weatherapp.api.WeatherApi
import com.reynard.weatherapp.model.response.WeatherResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Created by Reynard on January, 20 2024
 **
 * @author BCA Digital
 **/
class WeatherRepository @Inject constructor(private val api: WeatherApi) {
    fun getWeatherByCityName(cityName: String): Single<WeatherResponse> {
        return api.getWeather(cityName = cityName)
    }

    fun getWeatherByGeoLocation(latitude: Double, longitude: Double): Single<WeatherResponse> {
        return api.getWeather(latitude = latitude, longitude = longitude)
    }
}