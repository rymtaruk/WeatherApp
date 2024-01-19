package com.reynard.core.di.module

import com.google.gson.GsonBuilder
import com.reynard.core.configurations.RetrofitConfiguration
import com.reynard.core.constant.AppCoreConstant
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ProviderModule {
    @Singleton
    @Provides
    fun provideConfigRetrofit(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder().baseUrl(AppCoreConstant.baseUrl!!)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(RetrofitConfiguration.getClient().build())
            .build()
    }
}