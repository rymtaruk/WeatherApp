package com.reynard.core.configurations

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.reynard.core.constant.AppCoreConstant
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient

class RetrofitConfiguration {
    companion object {
        fun getClient(): OkHttpClient.Builder {
            val builder = OkHttpClient.Builder()

            val chuckInterceptor = ChuckerInterceptor.Builder(AppCoreConstant.context!!).build()
            builder.addInterceptor(Interceptor { chain ->
                val originalRequest = chain.request()
                val originalUrl = originalRequest.url()

                val newUrl: HttpUrl = originalUrl.newBuilder()
                    .addQueryParameter("appid", AppCoreConstant.appid!!)
                    .build()

                val newRequest = originalRequest.newBuilder().url(newUrl).build()

                return@Interceptor chain.proceed(newRequest)
            }).addInterceptor(chuckInterceptor)

            return builder
        }
    }
}