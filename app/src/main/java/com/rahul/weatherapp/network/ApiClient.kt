package com.rahul.weatherapp.network

import com.rahul.weatherapp.features.model.CityWeatherResponse
import com.rahul.weatherapp.features.model.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("weather")
    fun getWeatherDetails(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
    ): Call<CityWeatherResponse>

    @GET("direct")
    fun getSearchDetails(
        @Query("q") q: String,
        @Query("limit") limit: Int,
    ): Call<ArrayList<SearchResponse>>

}