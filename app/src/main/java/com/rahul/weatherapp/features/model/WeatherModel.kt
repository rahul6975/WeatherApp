package com.rahul.weatherapp.features.model

import com.rahul.weatherapp.common.RequestCompleteListener

interface WeatherModel {
    fun getWeatherInfo(
        lat: Double,
        lon: Double,
        callback: RequestCompleteListener<CityWeatherResponse>
    )

    fun getSearchList(
        q: String,
        limit: Int,
        callback: RequestCompleteListener<ArrayList<SearchResponse>>
    )

}