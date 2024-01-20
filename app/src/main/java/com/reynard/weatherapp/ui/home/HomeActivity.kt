package com.reynard.weatherapp.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.reynard.weatherapp.base.AppBaseActivity
import com.reynard.weatherapp.databinding.ActivityHomeBinding
import com.reynard.weatherapp.ui.favorite.FavoriteActivity
import com.reynard.weatherapp.ui.favorite.LATITUDE
import com.reynard.weatherapp.ui.favorite.LONGITUDE
import com.reynard.weatherapp.utils.IconUtil

class HomeActivity : AppBaseActivity<ActivityHomeBinding, HomeViewModel>(
    ActivityHomeBinding::inflate, HomeViewModel::class.java
) {
    private lateinit var weatherAdapter: HomeWeatherAdapter
    private var countDown: CountDownTimer? = null

    private val startFavoriteActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val extras = result.data?.extras!!
                val latitude = extras.getDouble(LATITUDE)
                val longitude = extras.getDouble(LONGITUDE)

                viewModel.getWeatherByLocation(longitude = longitude, latitude = latitude)
            }
        }

    override fun initView() {
        weatherAdapter = HomeWeatherAdapter()
    }

    override fun onViewCreated() {
        binding.apply {
            tvDate.text = viewModel.currentDate

            rvWeather.apply {
                layoutManager =
                    LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
                itemAnimator = null
            }
            rvWeather.adapter = weatherAdapter

            etSearch.addTextChangedListener(onTextChanged = { _, _, _, _ ->
                startTimer()
            })

            cvFavorites.setOnClickListener {
                startFavoriteActivity.launch(Intent(it.context, FavoriteActivity::class.java))
            }

            cvAddFav.setOnClickListener {
                if (viewModel.hasAddedFavorite.value == true){
                    Toast.makeText(this@HomeActivity, "Opps! City has been saved!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                viewModel.saveFavoriteCity(
                    latitude = viewModel.currentData.value?.coord?.lat ?: 0.0,
                    longitude = viewModel.currentData.value?.coord?.lon ?: 0.0
                )
                Toast.makeText(this@HomeActivity, "Yay! City saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onObservableViewModel() {
        viewModel.cityName.observe(this) {
            binding.tvLocation.text = it
        }

        viewModel.dataMap.observe(this) {
            binding.tvTomorrow.text = it.keys.toList()[1]
            binding.tvTheDayAfterTomorrow.text = it.keys.toList()[2]

            notifyAdapterWithData(0)
            setupDailyTab()
        }

        viewModel.loadingSearch.observe(this) {
            if (it) {
                binding.loadingSearch.visibility = View.VISIBLE
                binding.ivSearch.visibility = View.GONE
            } else {
                binding.loadingSearch.visibility = View.GONE
                binding.ivSearch.visibility = View.VISIBLE
            }
        }

        viewModel.currentData.observe(this) {
            binding.ivWeatherIcon.setImageDrawable(
                IconUtil.getWeatherIcon(
                    this@HomeActivity, it?.weather?.get(0)?.id ?: 0
                )
            )
            binding.tvCurrentDegrees.text = "${(it?.main?.temp ?: 0)}°C"
            binding.tvCurrentHum.text = "${(it?.main?.humidity ?: 0)}%"
            binding.tvCurrentWind.text = "${(it?.wind?.speed ?: 0)}m/s"
        }

        viewModel.favoriteData.observe(this) {
            Toast.makeText(this@HomeActivity, it.size.toString(), Toast.LENGTH_SHORT).show()
        }

        viewModel.hasAddedFavorite.observe(this) { hasAddedFavorite ->
            if (hasAddedFavorite) {
                binding.cvAddFav.setCardBackgroundColor(Color.YELLOW)
            } else {
                binding.cvAddFav.setCardBackgroundColor(Color.WHITE)
            }
        }
    }

    private fun setupDailyTab() {
        binding.apply {
            tvToday.setOnClickListener {
                vwSelected.animate().x(0f).withEndAction {
                        tvToday.setTypeface(tvToday.typeface, Typeface.NORMAL)
                        tvTomorrow.setTypeface(tvTomorrow.typeface, Typeface.BOLD)
                        tvTheDayAfterTomorrow.setTypeface(
                            tvTheDayAfterTomorrow.typeface, Typeface.NORMAL
                        )
                        notifyAdapterWithData(0)
                    }.duration = 200
            }

            tvTomorrow.setOnClickListener {
                vwSelected.animate().x(tvTomorrow.width.toFloat()).withEndAction {
                        tvToday.setTypeface(tvToday.typeface, Typeface.NORMAL)
                        tvTomorrow.setTypeface(tvTomorrow.typeface, Typeface.BOLD)
                        tvTheDayAfterTomorrow.setTypeface(
                            tvTheDayAfterTomorrow.typeface, Typeface.NORMAL
                        )
                        notifyAdapterWithData(1)
                    }.duration = 200
            }

            tvTheDayAfterTomorrow.setOnClickListener {
                vwSelected.animate().x(tvTomorrow.width.toFloat() * 2).withEndAction {
                        tvToday.setTypeface(tvToday.typeface, Typeface.NORMAL)
                        tvTomorrow.setTypeface(tvTomorrow.typeface, Typeface.NORMAL)
                        tvTheDayAfterTomorrow.setTypeface(
                            tvTheDayAfterTomorrow.typeface, Typeface.BOLD
                        )
                        notifyAdapterWithData(2)
                    }.duration = 200
            }
        }
    }

    private fun notifyAdapterWithData(index: Int) {
        val data = viewModel.dataMap.value!!
        val listData = data[data.keys.toList()[index]]!!
        weatherAdapter.items = listData
        weatherAdapter.notifyItemRangeChanged(0, listData.size)
    }

    private fun startTimer() {
        cancelTimer()
        countDown = object : CountDownTimer(500, 500) {
            override fun onTick(p0: Long) {
                return
            }

            override fun onFinish() {
                viewModel.getWeatherByCityName(binding.etSearch.text.toString())
            }
        }
        countDown!!.start()
    }

    private fun cancelTimer() {
        if (countDown != null) countDown!!.cancel()
    }
}