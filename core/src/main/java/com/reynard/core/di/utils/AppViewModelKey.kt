package com.reynard.core.di.utils

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass


@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@MustBeDocumented
@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class AppViewModelKey(val value: KClass<out ViewModel>)