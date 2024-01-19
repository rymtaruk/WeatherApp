package com.reynard.weatherapp.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

abstract class AppBaseViewModel : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val _defaultError: MutableLiveData<String> = MutableLiveData()
    private val _defaultUnauthorized: MutableLiveData<String> = MutableLiveData()
    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    private var loadingCounter = 0
    val loading: LiveData<Boolean> get() = _loading
    val defaultError: LiveData<String> get() = _defaultError
    val defaultUnauthorized: LiveData<String> get() = _defaultUnauthorized

    protected fun addDispose(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    protected fun <Any : kotlin.Any> setRepository(repository: Single<Any>): Maybe<Any> {
        return repository.toMaybe().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    protected open fun <T> showLoading(source: Maybe<T>): Maybe<T> {
        return source.doOnSubscribe {
            synchronized(_loading) {
                if (loadingCounter == 0) {
                    _loading.value = true
                }
                loadingCounter++
            }
        }.doAfterTerminate {
            synchronized(_loading) {
                loadingCounter--
                if (loadingCounter == 0) {
                    _loading.value = false
                }
            }
        }
    }

    protected open fun errorHandler(e: Throwable?, tag: String? = null): Boolean {
        when (e) {
            is ConnectException -> {
                _defaultError.value = tag
                return true
            }

            is UnknownHostException -> {
                _defaultError.value = tag
                return true
            }

            is HttpException -> {
                if (e.code() == 401) {
                    _defaultUnauthorized.value = tag
                    return true
                }
                _defaultError.value = tag
                return true
            }

            else -> {
                _defaultError.value = tag
                return true
            }
        }
    }
}