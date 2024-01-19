package com.reynard.core.configurations

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.reynard.core.constant.AppCoreConstant
import okhttp3.OkHttpClient

class RetrofitConfiguration {
    companion object {
        fun getClient(): OkHttpClient.Builder {
            val builder = OkHttpClient.Builder()

            val chuckInterceptor = ChuckerInterceptor.Builder(AppCoreConstant.context!!).build()
            builder.addInterceptor(chuckInterceptor)

            return builder
        }
    }
}