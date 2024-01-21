package com.reynard.weatherapp.viewModel

import com.google.gson.Gson
import com.reynard.weatherapp.api.repositories.WeatherRepository
import com.reynard.weatherapp.database.model.FavoriteData
import com.reynard.weatherapp.database.repositories.IFavoriteRepository
import com.reynard.weatherapp.dummy.FavoriteDummy
import com.reynard.weatherapp.ui.favorite.FavoriteViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by Reynard on January, 21 2024
 **
 * @author BCA Digital
 **/
class FavoriteViewModelTest {
    @Mock
    private lateinit var mockWeatherRepository: WeatherRepository

    @Mock
    private lateinit var mockFavoriteRepository: IFavoriteRepository

    private lateinit var favoriteViewModel: FavoriteViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        favoriteViewModel = FavoriteViewModel(
            weatherRepository = mockWeatherRepository,
            iFavoriteRepository = mockFavoriteRepository
        )
    }

    @Test
    fun getAllFavoriteDataSuccess() {
        Mockito.`when`(mockFavoriteRepository.getAllData())
            .thenReturn(Single.just(FavoriteDummy.createFavoritesDummy()))

        val testObserver = TestObserver<List<FavoriteData>>()
        mockFavoriteRepository.getAllData()
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .subscribe(testObserver)

        testObserver.assertNoErrors()
        testObserver.await()

        val expected: String = Gson().toJson(FavoriteDummy.createFavoritesDummy())

        testObserver.assertValue {
            Gson().toJson(it).equals(expected)
        }
    }

    @Test
    fun getAllFavoriteDataFailed() {
        Mockito.`when`(mockFavoriteRepository.getAllData())
            .thenReturn(Single.error(Throwable("No Data")))

        val testObserver = TestObserver<List<FavoriteData>>()
        mockFavoriteRepository.getAllData()
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .subscribe(testObserver)

        testObserver.assertError {
            it.message == "No Data"
        }
    }
}