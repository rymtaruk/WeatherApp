package com.reynard.weatherapp.viewModel

import com.google.gson.Gson
import com.reynard.weatherapp.api.repositories.WeatherRepository
import com.reynard.weatherapp.database.repositories.IFavoriteRepository
import com.reynard.weatherapp.dummy.WeatherDummy
import com.reynard.weatherapp.model.response.WeatherResponse
import com.reynard.weatherapp.ui.home.HomeViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by Reynard on January, 20 2024
 **
 * @author BCA Digital
 **/
class HomeViewModelTest {

    @Mock
    private lateinit var mockWeatherRepository: WeatherRepository

    @Mock
    private lateinit var mockFavoriteRepository: IFavoriteRepository

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        homeViewModel = HomeViewModel(
            weatherRepository = mockWeatherRepository,
            iFavoriteRepository = mockFavoriteRepository
        )
    }

    @Test
    fun getWeatherByNameSuccess() {
        Mockito.`when`(mockWeatherRepository.getWeatherByCityName("jakarta"))
            .thenReturn(Single.just(WeatherDummy.createDummyWeatherResponse()))

        val testObserver = TestObserver<WeatherResponse>()
        mockWeatherRepository.getWeatherByCityName("jakarta")
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.await()

        val expected: String = Gson().toJson(WeatherDummy.createDummyWeatherResponse())

        testObserver.assertValue {
            Gson().toJson(it).equals(expected)
        }
    }
    @Test
    fun getWeatherByNameError() {
        Mockito.`when`(mockWeatherRepository.getWeatherByCityName("jakarta"))
            .thenReturn(Single.error(Throwable("No Data")))

        val testObserver = TestObserver<WeatherResponse>()
        mockWeatherRepository.getWeatherByCityName("jakarta")
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .subscribe(testObserver)

        testObserver.assertError {
            it.message == "No Data"
        }
    }
    @Test
    fun getWeatherByLocationSuccess() {
        Mockito.`when`(mockWeatherRepository.getWeatherByGeoLocation(-6.2146, 106.8451))
            .thenReturn(Single.just(WeatherDummy.createDummyWeatherResponse()))

        val testObserver = TestObserver<WeatherResponse>()
        mockWeatherRepository.getWeatherByGeoLocation(-6.2146, 106.8451)
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.await()

        val expected: String = Gson().toJson(WeatherDummy.createDummyWeatherResponse())

        testObserver.assertValue {
            Gson().toJson(it).equals(expected)
        }
    }
    @Test
    fun getWeatherByLocationError() {
        Mockito.`when`(mockWeatherRepository.getWeatherByGeoLocation(-6.2146, 106.8451))
            .thenReturn(Single.error(Throwable("No Data")))

        val testObserver = TestObserver<WeatherResponse>()
        mockWeatherRepository.getWeatherByGeoLocation(-6.2146, 106.8451)
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .subscribe(testObserver)

        testObserver.assertError {
            it.message == "No Data"
        }
    }
}