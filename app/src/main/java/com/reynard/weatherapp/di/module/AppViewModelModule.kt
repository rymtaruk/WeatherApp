package com.reynard.weatherapp.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reynard.core.di.utils.AppViewModelKey
import com.reynard.core.di.utils.ViewModelFactory
import com.reynard.weatherapp.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AppViewModelModule {
    @Binds
    @IntoMap
    @AppViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory?): ViewModelProvider.Factory?
}