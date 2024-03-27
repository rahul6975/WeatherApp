package com.rahul.weatherapp.features.model

import android.content.Context
import com.rahul.weatherapp.common.RequestCompleteListener
import com.rahul.weatherapp.network.ApiClient
import com.rahul.weatherapp.network.SearchNetwork
import com.rahul.weatherapp.network.WeatherNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherModelImp(private val context: Context) : WeatherModel {
    override fun getWeatherInfo(
        lat: Double,
        lon: Double,
        callback: RequestCompleteListener<CityWeatherResponse>
    ) {


        val weatherApiInterface: ApiClient = WeatherNetwork.weatherClient.create(ApiClient::class.java)
        val call: Call<CityWeatherResponse> = weatherApiInterface.getWeatherDetails(lat, lon)

        call.enqueue(object : Callback<CityWeatherResponse> {

            // if retrofit network call success, this method will be triggered
            override fun onResponse(
                call: Call<CityWeatherResponse>,
                response: Response<CityWeatherResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!) //let presenter know the weather information data
                else
                    callback.onRequestFailed(response.message()) //let presenter know about failure
            }

            // this method will be triggered if network call failed
            override fun onFailure(call: Call<CityWeatherResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!) //let presenter know about failure
            }
        })

    }

    override fun getSearchList(
        q: String,
        limit: Int,
        callback: RequestCompleteListener<ArrayList<SearchResponse>>
    ) {

        val searchApiInterface: ApiClient = SearchNetwork.searchClient.create(ApiClient::class.java)
        val call: Call<ArrayList<SearchResponse>> = searchApiInterface.getSearchDetails(q, limit)

        call.enqueue(object : Callback<ArrayList<SearchResponse>> {

            // if retrofit network call success, this method will be triggered
            override fun onResponse(
                call: Call<ArrayList<SearchResponse>>,
                response: Response<ArrayList<SearchResponse>>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(response.body()!!) //let presenter know the weather information data
                else
                    callback.onRequestFailed(response.message()) //let presenter know about failure
            }

            // this method will be triggered if network call failed
            override fun onFailure(call: Call<ArrayList<SearchResponse>>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!) //let presenter know about failure
            }
        })

    }
}