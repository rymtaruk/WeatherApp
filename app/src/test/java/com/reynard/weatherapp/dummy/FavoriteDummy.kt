package com.reynard.weatherapp.dummy

import com.reynard.weatherapp.database.model.FavoriteData

/**
 * Created by Reynard on January, 21 2024
 **
 * @author BCA Digital
 **/
object FavoriteDummy {
    fun createFavoritesDummy(): List<FavoriteData> {
        val items = ArrayList<FavoriteData>()

        items.add(FavoriteData(id = 1, name = "Jakarta", latitude = 0.1, longitude = 0.31))
        items.add(FavoriteData(id = 2, name = "Bandung", latitude = 0.44, longitude = 0.3331))
        items.add(FavoriteData(id = 3, name = "Bogor", latitude = 0.553, longitude = 0.0042))
        items.add(FavoriteData(id = 3, name = "Makassar", latitude = 0.222, longitude = 0.665))

        return items
    }
}