package com.rahul.weatherapp.network

import okhttp3.Interceptor
import okhttp3.Response
import com.rahul.weatherapp.utils.ApiConstants

class QueryParameterAddInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val url = chain.request().url.newBuilder()
            .addQueryParameter("appid", ApiConstants.API_KEY)
            .build()

        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}