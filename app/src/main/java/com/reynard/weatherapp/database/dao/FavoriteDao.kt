package com.reynard.weatherapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.reynard.weatherapp.database.model.FavoriteData
import io.reactivex.rxjava3.core.Single


@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite_data WHERE id=:id")
    fun findDataById(id: Int): Single<FavoriteData>

    @Query("SELECT * FROM favorite_data WHERE latitude=:lat AND longitude=:lon")
    fun findDataByLatAndLon(lat: Double, lon: Double): Single<FavoriteData>

    @Query("SELECT * FROM favorite_data")
    fun getAllData(): Single<List<FavoriteData>>

    @Insert
    fun insert(favoriteData: FavoriteData)
}