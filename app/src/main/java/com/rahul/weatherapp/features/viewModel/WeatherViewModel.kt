package com.rahul.weatherapp.features.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rahul.weatherapp.common.RequestCompleteListener
import com.rahul.weatherapp.features.model.*
import com.rahul.weatherapp.utils.kelvinToCelsius
import com.rahul.weatherapp.utils.unixTimestampToDateTimeString
import com.rahul.weatherapp.utils.unixTimestampToTimeString

class WeatherViewModel : ViewModel() {

    val weatherLiveData = MutableLiveData<WeatherData>()
    val weatherFailureLiveData = MutableLiveData<String>()
    val searchLiveData = MutableLiveData<ArrayList<SearchList>>()
    val searchFailureLiveData = MutableLiveData<String>()
    val progressBarLiveData = MutableLiveData<Boolean>()


    fun getWeatherInfo(lat: Double, lon: Double, model: WeatherModel) {
        progressBarLiveData.postValue(true)
        model.getWeatherInfo(lat, lon, object : RequestCompleteListener<CityWeatherResponse> {
            override fun onRequestSuccess(data: CityWeatherResponse) {
                val weatherData = WeatherData(
                    dateTime = data.dt.unixTimestampToDateTimeString(),
                    temperature = data.main.temp.kelvinToCelsius().toString(),
                    cityAndCountry = "${data.name}, ${data.sys.country}",
                    weatherConditionIconUrl = "http://openweathermap.org/img/w/${data.weather.first().icon}.png",
                    weatherConditionIconDescription = data.weather.first().description,
                    humidity = "${data.main.humidity}%",
                    pressure = "${data.main.pressure} mBar",
                    visibility = "${data.visibility / 1000.0} KM",
                    sunrise = data.sys.sunrise.unixTimestampToTimeString(),
                    sunset = data.sys.sunset.unixTimestampToTimeString()
                )

                progressBarLiveData.postValue(false) // PUSH data to LiveData object to hide progress bar
                weatherLiveData.postValue(weatherData)
            }

            override fun onRequestFailed(errorMessage: String) {
                progressBarLiveData.postValue(false) // hide progress bar
                weatherFailureLiveData.postValue(errorMessage)
            }
        })
    }

    fun getSearchResult(q: String, model: WeatherModel) {
        progressBarLiveData.postValue(true)

        model.getSearchList(q, 5, object : RequestCompleteListener<ArrayList<SearchResponse>> {
            override fun onRequestSuccess(data: ArrayList<SearchResponse>) {
                val array = arrayListOf<SearchList>()

                data.forEach {
                    val item = SearchList()
                    item.name = it.name
                    item.lat = it.lat
                    item.lon = it.lon
                    array.add(item)
                }

                progressBarLiveData.postValue(false)
                searchLiveData.postValue(array)
            }

            override fun onRequestFailed(errorMessage: String) {
                progressBarLiveData.postValue(false)
                searchFailureLiveData.postValue(errorMessage)
            }
        })
    }
}