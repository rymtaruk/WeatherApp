package com.reynard.weatherapp.di.module

import com.reynard.weatherapp.ui.favorite.FavoriteActivity
import com.reynard.weatherapp.ui.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class AppViewModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    abstract fun contributeFavoriteActivity(): FavoriteActivity
}