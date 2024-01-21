package com.reynard.weatherapp.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.CountDownTimer
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.reynard.weatherapp.base.AppBaseActivity
import com.reynard.weatherapp.databinding.ActivityHomeBinding
import com.reynard.weatherapp.ui.favorite.CITY_NAME
import com.reynard.weatherapp.ui.favorite.FavoriteActivity
import com.reynard.weatherapp.utils.IconUtil

private const val LOCATION_PERMISSION_CODE = 101

class HomeActivity : AppBaseActivity<ActivityHomeBinding, HomeViewModel>(
    ActivityHomeBinding::inflate, HomeViewModel::class.java
) {
    private lateinit var weatherAdapter: HomeWeatherAdapter
    private var countDown: CountDownTimer? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val startFavoriteActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val extras = result.data?.extras!!
                val cityName = extras.getString(CITY_NAME) ?: ""

                binding.etSearch.text.clear()
                binding.etSearch.clearFocus()
                viewModel.getWeatherByCityName(cityName = cityName)
            }
        }

    override fun initView() {
        weatherAdapter = HomeWeatherAdapter()
        requestPermission()
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

            btnRefresh.setOnClickListener {
                viewModel.getWeatherByLocation(
                    longitude = viewModel.selectedLongitude,
                    latitude = viewModel.selectedLatitude
                )
            }

            root.setOnRefreshListener {
                viewModel.getWeatherByLocation(
                    longitude = viewModel.selectedLongitude,
                    latitude = viewModel.selectedLatitude
                )
            }

            cvAddFav.setOnClickListener {
                if (viewModel.hasAddedFavorite.value == true) {
                    Toast.makeText(
                        this@HomeActivity,
                        "Opps! City is already in favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.saveFavoriteCity()
                Toast.makeText(this@HomeActivity, "Yay! City is saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onObservableViewModel() {
        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) {
                binding.defaultLoading.visibility = View.VISIBLE
            } else {
                binding.root.isRefreshing = false
                binding.defaultLoading.visibility = View.GONE
            }
        }

        viewModel.currentCity.observe(this) {
            binding.tvLocation.text = it.name
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
                hideKeyboard()
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
                if (binding.etSearch.text.toString().isNotEmpty()) {
                    viewModel.getWeatherByCityName(binding.etSearch.text.toString())
                }
            }
        }
        countDown!!.start()
    }

    private fun cancelTimer() {
        if (countDown != null) countDown!!.cancel()
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                this@HomeActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // request permission
            ActivityCompat.requestPermissions(
                this@HomeActivity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_CODE
            )
        } else {
            getCurrentLocation()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission granted
                    getCurrentLocation()
                } else {
                    // permission denied
                    // default will using jakarta location
                    usingDefaultLocation()
                }
            }
        }
    }

    private fun usingDefaultLocation() {
        viewModel.selectedLatitude = -6.2146
        viewModel.selectedLongitude = 106.8451
        viewModel.getWeatherByLocation()
    }

    private fun getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@HomeActivity)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            usingDefaultLocation()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                viewModel.selectedLatitude = location.latitude
                viewModel.selectedLongitude = location.longitude
                viewModel.getWeatherByLocation()
            } else {
                usingDefaultLocation()
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
        binding.etSearch.clearFocus()
    }
}