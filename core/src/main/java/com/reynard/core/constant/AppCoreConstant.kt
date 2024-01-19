package com.reynard.core.constant

import android.annotation.SuppressLint
import android.content.Context

class AppCoreConstant {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        var baseUrl: String? = null
    }
}