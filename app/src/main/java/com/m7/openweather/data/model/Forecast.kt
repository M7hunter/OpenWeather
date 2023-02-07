package com.m7.openweather.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Forecast(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
) : Parcelable {
    val weatherItem get() = weather.firstOrNull()
}

@Parcelize
data class Main(
    val temp: Float,
    val feels_like: Float,
) : Parcelable {

    fun tempAsCelsius(): Float {
        return (temp * 9 / 5) + 32
    }
}

@Parcelize
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
) : Parcelable

@Parcelize
data class City(
    val id: Long,
    val name: String,
    val country: String,
    val coord: Coord,
) : Parcelable

@Parcelize
data class Coord(
    val lat: String,
    val lon: String,
) : Parcelable
