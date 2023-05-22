package com.example.weatherforecast.ui.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherDataNew(
    val cod: String? = null,
    val message: Int? = null,
    val cnt: Int? = null,
    val list: List<WeatherData>? = null
):Parcelable
@Parcelize
data class WeatherData(
    val dt: Long? = null,
    val main: WeatherMain? = null,
    val weather: List<Weather>? = null,
    val clouds: Clouds? = null,
    val wind: Wind? = null,
    val visibility: Int? = null,
    val pop: Double? = null,
    val rain: Rain? = null,
    val sys: Sys? = null,
    val dt_txt: String? = null
):Parcelable
@Parcelize
data class WeatherMain(
    val temp: Double? = null,
    val feels_like: Double? = null,
    val temp_min: Double? = null,
    val temp_max: Double? = null,
    val pressure: Int? = null,
    val sea_level: Int? = null,
    val grnd_level: Int? = null,
    val humidity: Int? = null,
    val temp_kf: Double? = null
):Parcelable
@Parcelize
data class Weather(
    val id: Int? = null,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null
):Parcelable
@Parcelize
data class Clouds(
    val all: Int? = null
):Parcelable
@Parcelize
data class Wind(
    val speed: Double? = null,
    val deg: Int? = null,
    val gust: Double? = null
):Parcelable
@Parcelize
data class Rain(
    val `3h`: Double? = null
):Parcelable
@Parcelize
data class Sys(
    val pod: String? = null
):Parcelable
