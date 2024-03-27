package com.rahul.weatherapp.network

import com.google.gson.GsonBuilder
import com.rahul.weatherapp.utils.ApiConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object WeatherNetwork {

    private var retrofit: Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()

    val weatherClient: Retrofit
        get() {
            if (retrofit == null) {
                synchronized(Retrofit::class.java) {
                    if (retrofit == null) {

                        val interceptor = HttpLoggingInterceptor().apply {
                            this.level = HttpLoggingInterceptor.Level.BODY
                        }
                        val httpClient = OkHttpClient.Builder()
                            .addInterceptor(QueryParameterAddInterceptor())
                        httpClient.addInterceptor(
                            interceptor
                        )
                        val client = httpClient.build()

                        retrofit = Retrofit.Builder()
                            .baseUrl(ApiConstants.WEATHER_BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .client(client)
                            .build()
                    }
                }

            }
            return retrofit!!
        }

}