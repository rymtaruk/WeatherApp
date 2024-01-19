package com.reynard.weatherapp.ui.home

import com.reynard.weatherapp.base.AppBaseActivity
import com.reynard.weatherapp.databinding.ActivityHomeBinding

class HomeActivity : AppBaseActivity<ActivityHomeBinding, HomeViewModel>(
    ActivityHomeBinding::inflate,
    HomeViewModel::class.java
) {
    override fun initView() {

    }

    override fun onViewCreated() {
        binding.apply {
            tvLocation.text = "..."
            tvDate.text = "10 Jan, 2024"
        }
    }

    override fun onObservableViewModel() {
        viewModel.cityName.observe(this) {
            binding.tvLocation.text = it
        }
    }
}