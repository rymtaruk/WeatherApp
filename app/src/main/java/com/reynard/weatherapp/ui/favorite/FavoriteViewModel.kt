package com.reynard.weatherapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.reynard.weatherapp.api.repositories.WeatherRepository
import com.reynard.weatherapp.base.AppBaseViewModel
import com.reynard.weatherapp.database.model.FavoriteData
import com.reynard.weatherapp.database.repositories.IFavoriteRepository
import com.reynard.weatherapp.model.response.CurrentWeatherResponse
import javax.inject.Inject

/**
 * Created by Reynard on January, 20 2024
 **
 * @author BCA Digital
 **/
class FavoriteViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val iFavoriteRepository: IFavoriteRepository
) : AppBaseViewModel() {
    private val _favoriteData = MutableLiveData<List<CurrentWeatherResponse>>()

    val favoriteData: LiveData<List<CurrentWeatherResponse>> get() = _favoriteData

    private val items = ArrayList<CurrentWeatherResponse>()
    init {
        getAllFavoriteData()
    }

    private fun getAllFavoriteData() {
        addDispose(
            setRepository(iFavoriteRepository.getAllData())
                .subscribe(this::getCurrentData) {
                    this.errorHandler(e = it)
                }
        )
    }

    private fun getCurrentData(favoritesData: List<FavoriteData>) {
        for (fav in favoritesData) {
            getCurrentWeather(latitude = fav.latitude, longitude = fav.longitude)
        }
    }

    private fun getCurrentWeather(latitude: Double, longitude: Double) {
        addDispose(
            setRepository(
                weatherRepository.getCurrentWeatherByLocation(
                    latitude = latitude,
                    longitude = longitude
                )
            ).subscribe(this::addToListFavorite) {
                this.errorHandler(e = it)
            }
        )
    }

    private fun addToListFavorite(data: CurrentWeatherResponse) {
        items.add(data)

        _favoriteData.value = items
    }
}