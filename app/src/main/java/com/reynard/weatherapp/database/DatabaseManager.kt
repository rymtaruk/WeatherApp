package com.reynard.weatherapp.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.reynard.weatherapp.database.dao.FavoriteDao
import com.reynard.weatherapp.database.model.FavoriteData


@Database(entities = [FavoriteData::class], version = 1, exportSchema = false)
abstract class DatabaseManager : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    companion object {
        fun getInstance(applicationContext: Application): DatabaseManager {
            return Room.databaseBuilder(
                applicationContext.applicationContext,
                DatabaseManager::class.java,
                DatabaseManager::class.java.name
            ).allowMainThreadQueries().build()
        }
    }

}