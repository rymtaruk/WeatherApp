package com.reynard.weatherapp.ui.favorite

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.reynard.weatherapp.base.AppBaseActivity
import com.reynard.weatherapp.databinding.ActivityFavoriteBinding


const val CITY_NAME = "CITY_NAME"

class FavoriteActivity : AppBaseActivity<ActivityFavoriteBinding, FavoriteViewModel>(
    ActivityFavoriteBinding::inflate,
    FavoriteViewModel::class.java
) {
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun initView() {
        favoriteAdapter = FavoriteAdapter()
    }

    override fun onViewCreated() {
        binding.apply {
            tvDate.text = viewModel.currentDate

            rvFavorite.apply {
                layoutManager =
                    LinearLayoutManager(this@FavoriteActivity, LinearLayoutManager.VERTICAL, false)
                itemAnimator = null
            }
            rvFavorite.adapter = favoriteAdapter
            favoriteAdapter.onClickItem = { cityName ->
                val intent = Intent()
                intent.putExtra(CITY_NAME, cityName)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            cvBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onObservableViewModel() {
        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) {
                binding.defaultLoading.visibility = View.VISIBLE
            } else {
                binding.defaultLoading.visibility = View.GONE
            }
        }

        viewModel.favoriteData.observe(this) {
            favoriteAdapter.items = it
            favoriteAdapter.notifyItemRangeChanged(0, it.size)
        }
    }
}