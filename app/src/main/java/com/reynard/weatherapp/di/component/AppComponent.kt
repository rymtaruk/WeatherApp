package com.reynard.weatherapp.di.component

import android.app.Application
import com.reynard.core.di.component.CoreComponent
import com.reynard.core.di.utils.AppScope
import com.reynard.weatherapp.di.injector.AppInjector
import com.reynard.weatherapp.di.module.AppApiModule
import com.reynard.weatherapp.di.module.AppViewModelModule
import com.reynard.weatherapp.di.module.AppViewModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule


@AppScope
@Component(
    modules = [AppApiModule::class, AppViewModule::class, AppViewModelModule::class, AndroidInjectionModule::class],
    dependencies = [CoreComponent::class]
)
interface AppComponent {
    fun inject(appInjector: AppInjector)

    @Component.Builder
    interface Build {
        @BindsInstance
        fun application(application: Application) : Build
        fun coreComponent(coreComponent: CoreComponent) : Build
        fun build() : AppComponent
    }
}