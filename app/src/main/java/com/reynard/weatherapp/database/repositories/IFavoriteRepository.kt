package com.reynard.weatherapp.database.repositories

import com.reynard.weatherapp.database.model.FavoriteData
import io.reactivex.rxjava3.core.Single

/**
 * Created by Reynard on January, 20 2024
 **
 * @author BCA Digital
 **/
interface IFavoriteRepository {
    fun findDataById(id: Int): Single<FavoriteData>
    fun findDataByLatAndLon(latitude: Double, longitude: Double): Single<FavoriteData>
    fun getAllData(): Single<List<FavoriteData>>
    fun save(favoriteData: FavoriteData)
}