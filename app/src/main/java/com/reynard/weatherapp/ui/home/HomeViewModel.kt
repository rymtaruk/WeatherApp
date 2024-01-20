package com.reynard.weatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.reynard.weatherapp.api.repositories.WeatherRepository
import com.reynard.weatherapp.base.AppBaseViewModel
import com.reynard.weatherapp.database.model.FavoriteData
import com.reynard.weatherapp.database.repositories.IFavoriteRepository
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
    private val _cityName = MutableLiveData<String>()
    private val _loadingSearch = MutableLiveData<Boolean>()
    private val _dataMap = MutableLiveData<MutableMap<String, MutableList<FourCastData>>>()
    private val _currentData = MutableLiveData<CurrentWeatherResponse>()
    private val _favoriteData = MutableLiveData<List<FavoriteData>>()
    private val _hasAddedFavorite = MutableLiveData<Boolean>()

    val cityName: LiveData<String> get() = _cityName
    val dataMap: LiveData<MutableMap<String, MutableList<FourCastData>>> get() = _dataMap
    val currentData: LiveData<CurrentWeatherResponse> get() = _currentData
    val loadingSearch: LiveData<Boolean> get() = _loadingSearch
    val favoriteData: LiveData<List<FavoriteData>> get() = _favoriteData
    val hasAddedFavorite: LiveData<Boolean> get() = _hasAddedFavorite

    var selectedLatitude: Double = 0.0
    var selectedLongitude: Double = 0.0

    fun getWeatherByLocation(latitude: Double = selectedLatitude, longitude: Double = selectedLongitude) {
        selectedLatitude = latitude
        selectedLongitude = longitude
        addDispose(
            setRepository(
                weatherRepository.getWeatherByGeoLocation(longitude = longitude, latitude = latitude)
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
                    selectedLatitude = result?.city?.coord?.lat ?: 0.0
                    selectedLongitude = result?.city?.coord?.lon ?: 0.0
                    this.getCurrentWeather(
                        latitude = selectedLatitude,
                        longitude = selectedLongitude
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
                val result = it
                this.getFavoriteDataByName(
                    name = result.name
                )
                _currentData.value = result
            }, {
                this.errorHandler(e = it)
            })
        )
    }

    private fun getFavoriteDataByName(name: String) {
        addDispose(
            setRepository(
                iFavoriteRepository.findDataByName(
                    name = name,
                )
            )
                .subscribe({
                    _hasAddedFavorite.value = true
                }, {
                    _hasAddedFavorite.value = false
                })
        )
    }

    fun saveFavoriteCity(name: String, latitude: Double, longitude: Double) {
        iFavoriteRepository.save(
            favoriteData = FavoriteData(
                name = name,
                latitude = latitude,
                longitude = longitude
            )
        )

        this.getFavoriteDataByName(
            name = name
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