package com.reynard.weatherapp.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reynard.weatherapp.databinding.ItemFavoriteBinding
import com.reynard.weatherapp.model.response.CurrentWeatherResponse
import com.reynard.weatherapp.utils.IconUtil

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    var items: List<CurrentWeatherResponse?>? = null
    var onClickItem: ((String) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (items == null) {
            0
        } else {
            items!!.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items?.get(position)!!

        holder.view.ivWeatherIcon.setImageDrawable(
            IconUtil.getWeatherIcon(
                holder.view.root.context,
                data.weather[0].id ?: 0
            )
        )
        holder.view.tvName.text = data.name
        holder.view.tvCondition.text = data.weather[0].description

        holder.view.root.setOnClickListener {
            onClickItem?.let {
                onclick -> onclick(data.name)
            }
        }
    }

    inner class ViewHolder(binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding
    }
}