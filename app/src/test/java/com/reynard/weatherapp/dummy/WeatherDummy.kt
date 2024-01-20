package com.reynard.weatherapp.dummy

import com.reynard.weatherapp.model.data.CityData
import com.reynard.weatherapp.model.data.CloudsData
import com.reynard.weatherapp.model.data.CoordinateData
import com.reynard.weatherapp.model.data.FourCastData
import com.reynard.weatherapp.model.data.MainData
import com.reynard.weatherapp.model.data.SysData
import com.reynard.weatherapp.model.data.WeatherData
import com.reynard.weatherapp.model.data.WindData
import com.reynard.weatherapp.model.response.WeatherResponse

/**
 * Created by Reynard on January, 20 2024
 **
 * @author BCA Digital
 **/
object WeatherDummy {
    fun createDummyWeatherResponse(): WeatherResponse {
        return WeatherResponse(
            cod = "200",
            message = 0,
            cnt = 3,
            list = createDummyWeatherItems(),
            city = createDummyCity()
        )
    }

    private fun createDummyWeatherItems(): List<FourCastData> {
        return listOf(
            createDummyWeatherItem(
                1705762800,
                301.79,
                304.31,
                301.62,
                301.79,
                1010,
                1010,
                "scattered clouds",
                "03n",
                35,
                10.0
            ),
            createDummyWeatherItem(
                1705773600,
                300.95,
                303.31,
                300.49,
                300.95,
                1009,
                1009,
                "broken clouds",
                "04n",
                61,
                12.0
            ),
            createDummyWeatherItem(
                1705784400,
                300.03,
                302.04,
                300.03,
                300.03,
                1009,
                1009,
                "overcast clouds",
                "04n",
                100,
                11.0
            )
        )
    }

    private fun createDummyWeatherItem(
        dt: Long,
        temp: Double,
        feelsLike: Double,
        tempMin: Double,
        tempMax: Double,
        pressure: Int,
        humidity: Int,
        description: String,
        icon: String,
        id: Int,
        all: Double
    ): FourCastData {
        return FourCastData(
            dt = dt,
            main = MainData(temp, feelsLike, tempMin, tempMax, pressure, humidity),
            weather = listOf(
                WeatherData(
                    main = "Clouds",
                    description = description,
                    icon = icon,
                    id = id
                )
            ),
            clouds = CloudsData(all),
            wind = WindData(4.55, 239),
            visibility = 10000,
            pop = 0.02,
            sys = SysData(pod = "n"),
            dtTxt = "2024-01-20 15:00:00"
        )
    }

    private fun createDummyCity(): CityData {
        return CityData(
            id = 1642911,
            name = "Jakarta",
            coord = CoordinateData(lat = -6.2146, lon = 106.8451),
            country = "ID",
            population = 8540121,
            timezone = 25200,
            sunrise = 1705704621,
            sunset = 1705749358
        )
    }
}