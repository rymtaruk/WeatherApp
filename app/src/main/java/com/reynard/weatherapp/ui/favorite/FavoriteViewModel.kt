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

    fun getAllFavoriteData() {
        addDispose(
            setRepository(iFavoriteRepository.getAllData())
                .compose(this::showLoading)
                .subscribe(this::getCurrentData, this::errorHandler)
        )
    }

    private fun getCurrentData(favoritesData: List<FavoriteData>) {
        for (fav in favoritesData) {
            getCurrentWeather(fav)
        }
    }

    private fun getCurrentWeather(favData: FavoriteData) {
        addDispose(
            setRepository(
                weatherRepository.getCurrentWeatherByLocation(
                    latitude = favData.latitude,
                    longitude = favData.longitude
                )
            ).compose(this::showLoading)
                .subscribe(
                    { this.addToListFavorite(data = it, favData = favData) },
                    this::errorHandler
                )
        )
    }

    private fun addToListFavorite(data: CurrentWeatherResponse, favData: FavoriteData) {
        data.name = favData.name
        items.add(data)

        _favoriteData.value = items
    }
}