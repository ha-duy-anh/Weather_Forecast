package com.example.weatherforecast.ui.main

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherAPI {
    @GET("data/2.5/forecast")
    suspend fun getWeatherData(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiKey: String
    ): WeatherDataNew
}