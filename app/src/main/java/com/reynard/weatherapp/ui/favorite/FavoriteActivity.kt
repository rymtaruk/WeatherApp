package com.reynard.weatherapp.ui.favorite

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.reynard.weatherapp.base.AppBaseActivity
import com.reynard.weatherapp.databinding.ActivityFavoriteBinding

const val LATITUDE = "LATITUDE"
const val LONGITUDE = "LONGITUDE"

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
            rvFavorite.apply {
                layoutManager =
                    LinearLayoutManager(this@FavoriteActivity, LinearLayoutManager.VERTICAL, false)
                itemAnimator = null
            }
            rvFavorite.adapter = favoriteAdapter
            favoriteAdapter.onClickItem = { latitude, longitude ->
                val intent = Intent()
                intent.putExtra(LATITUDE, latitude)
                intent.putExtra(LONGITUDE, longitude)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onObservableViewModel() {
        viewModel.favoriteData.observe(this) {
            favoriteAdapter.items = it
            favoriteAdapter.notifyItemRangeChanged(0, it.size)
        }
    }
}