package com.rahul.weatherapp.features.model

data class WeatherData(
    var dateTime: String = "",
    var temperature: String = "0",
    var cityAndCountry: String = "",
    var weatherConditionIconUrl: String = "",
    var weatherConditionIconDescription: String = "",
    var humidity: String = "",
    var pressure: String = "",
    var visibility: String = "",
    var sunrise: String = "",
    var sunset: String = ""
)