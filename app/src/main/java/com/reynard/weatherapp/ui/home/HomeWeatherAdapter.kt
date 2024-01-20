package com.reynard.weatherapp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reynard.weatherapp.R
import com.reynard.weatherapp.databinding.ItemWeatherBinding
import com.reynard.weatherapp.model.data.FourCastData
import com.reynard.weatherapp.utils.IconUtil
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by Reynard on January, 20 2024
 **
 * @author BCA Digital
 **/
class HomeWeatherAdapter : RecyclerView.Adapter<HomeWeatherAdapter.ViewHolder>() {
    var items: List<FourCastData?>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (items == null) {
            0
        } else {
            items!!.size
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items?.get(position)

        holder.view.ivWeatherIcon.setImageDrawable(
            IconUtil.getWeatherIcon(
                holder.view.root.context,
                data?.weather?.get(0)?.id ?: 0
            )
        )
        holder.view.tvDegrees.text = "${(data?.main?.temp ?: 0)}°C"
        holder.view.tvHumidity.text = "${(data?.main?.humidity ?: 0)}%"
        holder.view.tvWindConditions.text = "${(data?.wind?.speed ?: 0)}m/s"

        val convertDate = Date(data?.dt!! * 1000)

        val timeFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())
        val formattedTime = timeFormat.format(convertDate)
        holder.view.tvTime.text = formattedTime
    }

    inner class ViewHolder(binding: ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding
    }
}