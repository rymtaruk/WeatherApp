package com.reynard.weatherapp.api

import com.reynard.weatherapp.model.response.CurrentWeatherResponse
import com.reynard.weatherapp.model.response.WeatherResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Reynard on January, 19 2024
 **
 * @author BCA Digital
 **/
interface WeatherApi {
    @GET("/data/2.5/forecast?units=metric")
    fun getWeather(
        @Query("q") cityName: String? = null,
        @Query("lat") latitude: Double? = null,
        @Query("lon") longitude: Double? = null
    ): Single<WeatherResponse>
    @GET("/data/2.5/weather?units=metric")
    fun getCurrentWeather(
        @Query("lat") latitude: Double? = null,
        @Query("lon") longitude: Double? = null
    ): Single<CurrentWeatherResponse>
}