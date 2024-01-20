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

    @Query("SELECT * FROM favorite_data WHERE name=:name")
    fun findDataByName(name: String): Single<FavoriteData>

    @Query("SELECT * FROM favorite_data")
    fun getAllData(): Single<List<FavoriteData>>

    @Insert
    fun insert(favoriteData: FavoriteData)
}