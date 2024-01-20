package com.reynard.weatherapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import com.reynard.weatherapp.R

/**
 * Created by Reynard on January, 20 2024
 **
 * @author BCA Digital
 **/
object IconUtil {

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getWeatherIcon(context: Context, id: Int): Drawable {
        if (id == 800) {
            return context.getDrawable(R.drawable.ic_clear_sky)!!
        } else if (id > 800) {
            return context.getDrawable(R.drawable.ic_scattered_cloud)!!
        } else if (id >= 700) {
            return context.getDrawable(R.drawable.ic_mist)!!
        } else if (id >= 600) {
            return context.getDrawable(R.drawable.ic_show)!!
        } else if (id >= 500) {
            return context.getDrawable(R.drawable.ic_rain)!!
        } else if (id >= 300) {
            return context.getDrawable(R.drawable.ic_shower_rain)!!
        } else if (id >= 200) {
            return context.getDrawable(R.drawable.ic_thunderstorm)!!
        } else {
            return context.getDrawable(R.drawable.ic_clear_sky)!!
        }
    }
}