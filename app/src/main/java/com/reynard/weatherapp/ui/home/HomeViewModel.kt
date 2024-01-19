package com.reynard.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.reynard.weatherapp.api.repositories.WeatherRepository
import com.reynard.weatherapp.base.AppBaseViewModel
import javax.inject.Inject

/**
 * Created by Reynard on January, 19 2024
 **
 * @author BCA Digital
 **/
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : AppBaseViewModel() {
    private val _cityName = MutableLiveData<String>()

    val cityName: LiveData<String> get() = _cityName

    init {
        getWeatherByLocation()
    }

    private fun getWeatherByLocation() {
        addDispose(
            setRepository(
                weatherRepository.getWeatherByGeoLocation(longitude = 106.8451, latitude = -6.2146)
            ).compose(this::showLoading)
                .subscribe({
                    _cityName.value = it.city?.name ?: ""
                }, {
                    this.errorHandler(e = it)
                })
        )
    }
}