package com.rahul.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.rahul.weatherapp.common.RequestCompleteListener
import com.rahul.weatherapp.features.model.*
import com.rahul.weatherapp.features.viewModel.WeatherViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyZeroInteractions
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var model: WeatherModel

    @Mock
    private lateinit var weatherObserver: Observer<WeatherData>

    @Mock
    private lateinit var weatherFailureObserver: Observer<String>

    @Mock
    private lateinit var searchObserver: Observer<ArrayList<SearchList>>

    @Mock
    private lateinit var searchFailureObserver: Observer<String>

    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setUp() {
        viewModel = WeatherViewModel()
        viewModel.weatherLiveData.observeForever(weatherObserver)
        viewModel.weatherFailureLiveData.observeForever(weatherFailureObserver)
        viewModel.searchLiveData.observeForever(searchObserver)
        viewModel.searchFailureLiveData.observeForever(searchFailureObserver)
    }

    @Test
    fun `test getting weather info`() {
        val lat = 37.7749
        val lon = -122.4194
        val cityWeatherResponse = CityWeatherResponse(
            "",
            Clouds(0), 0,
            Coord(1.0, 1.0), 0, 0, Main(0.0, 0, 0, 0.0, 0.0, 0.0), "", Sys("", 0, 0, 0, 0), 0, 0,
            listOf(), Wind(0, 0.0)
        )
        whenever(model.getWeatherInfo(any(), any(), any())).thenAnswer {
            val callback = it.arguments[2] as RequestCompleteListener<CityWeatherResponse>
            callback.onRequestSuccess(cityWeatherResponse)
        }

        viewModel.getWeatherInfo(lat, lon, model)

        verify(weatherObserver).onChanged(any())
        verifyZeroInteractions(weatherFailureObserver)
    }

    @Test
    fun `test failure in getting weather info`() {
        val lat = 37.7749
        val lon = -122.4194
        val errorMessage = "Weather info request failed"
        whenever(model.getWeatherInfo(any(), any(), any())).thenAnswer {
            val callback = it.arguments[2] as RequestCompleteListener<*>
            callback.onRequestFailed(errorMessage)
        }

        viewModel.getWeatherInfo(lat, lon, model)

        verify(weatherFailureObserver).onChanged(errorMessage)
        verifyZeroInteractions(weatherObserver)
    }

    @Test
    fun `test getting search result`() {
        val query = "New York"
        val searchResponseList = ArrayList<SearchResponse>()
        // fill searchResponseList with test data
        whenever(model.getSearchList(any(), any(), any())).thenAnswer {
            val callback = it.arguments[2] as RequestCompleteListener<ArrayList<SearchResponse>>
            callback.onRequestSuccess(searchResponseList)
        }

        viewModel.getSearchResult(query, model)

        verify(searchObserver).onChanged(any())
        verifyZeroInteractions(searchFailureObserver)
    }

    @Test
    fun `test failure in getting search result`() {
        val query = "New York"
        val errorMessage = "Search request failed"
        whenever(model.getSearchList(any(), any(), any())).thenAnswer {
            val callback = it.arguments[2] as RequestCompleteListener<ArrayList<SearchResponse>>
            callback.onRequestFailed(errorMessage)
        }

        viewModel.getSearchResult(query, model)

        verify(searchFailureObserver).onChanged(errorMessage)
        verifyZeroInteractions(searchObserver)
    }
}
