package com.reynard.weatherapp.di.injector

import android.app.Application
import android.content.Context
import com.reynard.core.constant.AppCoreConstant
import com.reynard.core.di.component.CoreComponent
import com.reynard.core.di.component.CoreComponentProvider
import com.reynard.core.di.component.DaggerCoreComponent
import com.reynard.weatherapp.di.component.AppComponent
import com.reynard.weatherapp.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

/**
 * Created by Reynard on January, 19 2024
 **
 * @author BCA Digital
 **/
class AppInjector(applicationContext: Application) : HasAndroidInjector, CoreComponentProvider {
    private var _appComponent: AppComponent? = null
    private var _coreComponent: CoreComponent? = null
    private val appComponent get() = _appComponent!!
    private val coreComponent get() = _coreComponent!!

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    init {
        _appComponent = DaggerAppComponent
            .builder()
            .application(applicationContext)
            .coreComponent(provideCoreComponent())
            .build()

        appComponent.inject(this)
    }

    companion object {
        private var instance: AppInjector? = null
        private fun initialize(applicationContext: Application) {
            AppCoreConstant.context = applicationContext
            AppCoreConstant.baseUrl = "https://api.openweathermap.org"
            AppCoreConstant.appid = "b72124bc9fc23e81d93bda14d32d7619"
            instance = AppInjector(applicationContext)
        }

        fun getInstance(context: Context): AppInjector {
            if (instance == null) {
                initialize(context.applicationContext as Application)
            }
            return instance!!
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }

    override fun provideCoreComponent(): CoreComponent {
        if (_coreComponent == null) {
            _coreComponent = DaggerCoreComponent.builder().build()
        }
        return coreComponent
    }
}