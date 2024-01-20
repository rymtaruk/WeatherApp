package com.reynard.weatherapp.ui.favorite

import com.reynard.weatherapp.api.repositories.WeatherRepository
import com.reynard.weatherapp.base.AppBaseViewModel
import com.reynard.weatherapp.database.repositories.IFavoriteRepository
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
}