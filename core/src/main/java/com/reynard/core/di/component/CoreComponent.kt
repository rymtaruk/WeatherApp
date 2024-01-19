package com.reynard.core.di.component

import com.reynard.core.di.module.ProviderModule
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Reynard on January, 19 2024
 **
 * @author BCA Digital
 **/

@Singleton
@Component(modules = [ProviderModule::class])
interface CoreComponent {
    fun provideRetrofitConfig() : Retrofit
}

interface CoreComponentProvider {
    fun provideCoreComponent() : CoreComponent
}