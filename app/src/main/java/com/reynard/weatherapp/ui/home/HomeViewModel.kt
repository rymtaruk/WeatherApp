package com.reynard.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.reynard.weatherapp.api.repositories.WeatherRepository
import com.reynard.weatherapp.base.AppBaseViewModel
import com.reynard.weatherapp.database.model.FavoriteData
import com.reynard.weatherapp.database.repositories.IFavoriteRepository
import com.reynard.weatherapp.model.data.CityData
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
    private val weatherRepository: WeatherRepository,
    private val iFavoriteRepository: IFavoriteRepository
) : AppBaseViewModel() {
    private val _loadingSearch = MutableLiveData<Boolean>()
    private val _dataMap = MutableLiveData<MutableMap<String, MutableList<FourCastData>>>()
    private val _currentData = MutableLiveData<CurrentWeatherResponse>()
    private val _currentCity = MutableLiveData<CityData>()
    private val _favoriteData = MutableLiveData<List<FavoriteData>>()
    private val _hasAddedFavorite = MutableLiveData<Boolean>()

    val dataMap: LiveData<MutableMap<String, MutableList<FourCastData>>> get() = _dataMap
    val currentData: LiveData<CurrentWeatherResponse> get() = _currentData
    val currentCity: LiveData<CityData> get() = _currentCity
    val loadingSearch: LiveData<Boolean> get() = _loadingSearch
    val favoriteData: LiveData<List<FavoriteData>> get() = _favoriteData
    val hasAddedFavorite: LiveData<Boolean> get() = _hasAddedFavorite

    var selectedLatitude: Double = 0.0
    var selectedLongitude: Double = 0.0

    fun getWeatherByLocation(
        latitude: Double = selectedLatitude,
        longitude: Double = selectedLongitude
    ) {
        selectedLatitude = latitude
        selectedLongitude = longitude
        addDispose(
            setRepository(
                weatherRepository.getWeatherByGeoLocation(
                    longitude = longitude,
                    latitude = latitude
                )
            ).compose(this::showLoading)
                .subscribe(
                    this::handleWeatherResponse,
                    this::errorHandler
                )
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
                .subscribe(
                    this::handleWeatherResponse,
                    this::errorHandler
                )
        )
    }

    private fun handleWeatherResponse(weatherResponse: WeatherResponse) {
        selectedLatitude = weatherResponse.city?.coord?.lat ?: 0.0
        selectedLongitude = weatherResponse.city?.coord?.lon ?: 0.0
        this.getCurrentWeather(
            latitude = selectedLatitude,
            longitude = selectedLongitude
        )
        this.mapDataByDate(weatherResponse)

        _currentCity.value = weatherResponse.city
    }

    private fun getCurrentWeather(latitude: Double, longitude: Double) {
        addDispose(
            setRepository(
                weatherRepository.getCurrentWeatherByLocation(
                    latitude = latitude,
                    longitude = longitude
                )
            ).subscribe({
                val result = it
                this.getFavoriteDataByName()
                _currentData.value = result
            }, {
                this.errorHandler(e = it)
            })
        )
    }

    private fun getFavoriteDataByName() {
        addDispose(
            setRepository(
                iFavoriteRepository.findDataByName(
                    name = currentCity.value?.name ?: "",
                )
            )
                .subscribe({
                    _hasAddedFavorite.value = true
                }, {
                    _hasAddedFavorite.value = false
                })
        )
    }

    fun saveFavoriteCity() {
        iFavoriteRepository.save(
            favoriteData = FavoriteData(
                id = currentCity.value?.id ?: 0,
                name = currentCity.value?.name ?: "",
                latitude = currentCity.value?.coord?.lat ?: 0.0,
                longitude = currentCity.value?.coord?.lon ?: 0.0
            )
        )

        this.getFavoriteDataByName()
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