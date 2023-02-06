package com.m7.openweather.data.model

data class Forecast(
    val dt: Long,
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>,
)

data class Main(
    val temp: Float,
    val feels_like: Float,
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)
