package com.reynard.weatherapp.di.module

import androidx.lifecycle.ViewModelProvider
import com.reynard.core.di.utils.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class AppViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory?): ViewModelProvider.Factory?
}