package com.reynard.weatherapp.ui.favorite

import com.reynard.weatherapp.base.AppBaseActivity
import com.reynard.weatherapp.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppBaseActivity<ActivityFavoriteBinding, FavoriteViewModel>(
    ActivityFavoriteBinding::inflate,
    FavoriteViewModel::class.java
) {
    override fun initView() {

    }

    override fun onViewCreated() {
    }

    override fun onObservableViewModel() {
    }
}