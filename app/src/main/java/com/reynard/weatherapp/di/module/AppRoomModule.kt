package com.reynard.weatherapp.di.module

import com.reynard.weatherapp.database.repositories.IFavoriteRepository
import com.reynard.weatherapp.database.repositories.impl.FavoriteRepositoryImpl
import dagger.Binds
import dagger.Module

/**
 * Created by Reynard on January, 20 2024
 **
 * @author BCA Digital
 **/
@Module
abstract class AppRoomModule {

    @Binds
    abstract fun provideFavoriteRepository(favoriteRepository: FavoriteRepositoryImpl): IFavoriteRepository
}