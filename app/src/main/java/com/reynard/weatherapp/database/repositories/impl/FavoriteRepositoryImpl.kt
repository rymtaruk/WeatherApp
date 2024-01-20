package com.reynard.weatherapp.database.repositories.impl

import android.app.Application
import com.reynard.weatherapp.database.DatabaseManager
import com.reynard.weatherapp.database.model.FavoriteData
import com.reynard.weatherapp.database.repositories.IFavoriteRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


class FavoriteRepositoryImpl @Inject constructor(private val applicationContext: Application): IFavoriteRepository{
    private var _databaseManager: DatabaseManager? = null

    private val databaseManager get() = _databaseManager!!

    init {
        _databaseManager = DatabaseManager.getInstance(applicationContext = applicationContext)
    }

    override fun findDataById(id: Int): Single<FavoriteData> {
        return databaseManager.favoriteDao().findDataById(id)
    }

    override fun findDataByLatAndLon(latitude: Double, longitude: Double): Single<FavoriteData> {
        return databaseManager.favoriteDao().findDataByLatAndLon(latitude, longitude)
    }

    override fun getAllData(): Single<List<FavoriteData>> {
        return databaseManager.favoriteDao().getAllData()
    }

    override fun save(favoriteData: FavoriteData) {
        databaseManager.favoriteDao().insert(favoriteData)
    }

}