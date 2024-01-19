package com.reynard.weatherapp.di.module

import com.reynard.weatherapp.api.WeatherApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class AppApiModule {
    @Provides
    fun provideRetrofitToWeatherApi(retrofit: Retrofit) : WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }
}