package com.reynard.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.reynard.weatherapp.api.repositories.WeatherRepository
import com.reynard.weatherapp.base.AppBaseViewModel
import com.reynard.weatherapp.model.data.FourCastData
import com.reynard.weatherapp.model.response.CurrentWeatherResponse
import com.reynard.weatherapp.model.response.WeatherResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
    private val _loadingSearch = MutableLiveData<Boolean>()
    private val _dataMap = MutableLiveData<MutableMap<String, MutableList<FourCastData>>>()
    private val _currentData = MutableLiveData<CurrentWeatherResponse>()

    val cityName: LiveData<String> get() = _cityName
    val dataMap: LiveData<MutableMap<String, MutableList<FourCastData>>> get() = _dataMap
    val currentData: LiveData<CurrentWeatherResponse> get() = _currentData
    val loadingSearch: LiveData<Boolean> get() = _loadingSearch


    init {
        getWeatherByLocation()
    }

    private fun getWeatherByLocation() {
        addDispose(
            setRepository(
                weatherRepository.getWeatherByGeoLocation(longitude = 106.8451, latitude = -6.2146)
            ).compose(this::showLoading)
                .subscribe({
                    val result = it
                    this.getCurrentWeather(
                        latitude = result.city?.coord?.lat ?: 0.0,
                        longitude = result.city?.coord?.lon ?: 0.0
                    )
                    mapDataByDate(result)

                    _cityName.value = result.city?.name ?: ""
                }, {
                    this.errorHandler(e = it)
                })
        )
    }

    fun getWeatherByCityName(cityName: String) {
        addDispose(
            setRepository(
                weatherRepository.getWeatherByCityName(cityName = cityName)
            )
                .doOnSubscribe {
                    _loadingSearch.value = true
                }
                .doAfterTerminate {
                    _loadingSearch.value = false
                }
                .subscribe({
                    val result = it
                    this.getCurrentWeather(
                        latitude = result?.city?.coord?.lat ?: 0.0,
                        longitude = result?.city?.coord?.lon ?: 0.0
                    )
                    this.mapDataByDate(it)

                    _cityName.value = it.city?.name ?: ""
                }, {
                    this.errorHandler(e = it)
                })
        )
    }

    private fun getCurrentWeather(latitude: Double, longitude: Double) {
        addDispose(
            setRepository(
                weatherRepository.getCurrentWeatherByLocation(
                    latitude = latitude,
                    longitude = longitude
                )
            ).subscribe({
                _currentData.value = it
            }, {
                this.errorHandler(e = it)
            })
        )
    }

    private fun mapDataByDate(weatherResponse: WeatherResponse) {
        val dataMap: MutableMap<String, MutableList<FourCastData>> = mutableMapOf()

        for (dataEntry in weatherResponse.list) {
            val timestamp = dataEntry?.dt ?: 0

            val convertDate = Date(timestamp * 1000)

            val dateFormat = SimpleDateFormat("E, dd MMM", Locale.getDefault())
            val formattedDate = dateFormat.format(convertDate)

            if (dataMap.containsKey(formattedDate)) {
                dataMap[formattedDate]!!.add(dataEntry!!)
            } else {
                dataMap[formattedDate] = mutableListOf(dataEntry!!)
            }
        }

        _dataMap.value = dataMap
    }
}