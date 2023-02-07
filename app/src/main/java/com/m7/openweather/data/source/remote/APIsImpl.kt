package com.m7.openweather.data.source.remote

import com.m7.openweather.BuildConfig
import javax.inject.Inject

class APIsImpl @Inject constructor(private val apIs: APIs) {

    suspend fun getWeatherByLatLng(lat: String, lng: String) =
        apIs.weatherByLatLng(lat, lng, BuildConfig.API_KEY)

    suspend fun getWeatherByCityName(cityName: String) =
        apIs.weatherByCityName(cityName, BuildConfig.API_KEY)

    suspend fun getWeatherByZip(zip: String) =
        apIs.weatherByZip(zip, BuildConfig.API_KEY)

    suspend fun getForecastByLatLng(lat: String, lng: String) =
        apIs.forecastByLatLng(lat, lng, BuildConfig.API_KEY, "5")

    suspend fun getForecastByCityName(cityName: String) =
        apIs.forecastByCityName(cityName, BuildConfig.API_KEY, "5")

    suspend fun getForecastByZip(zip: String) =
        apIs.forecastByZip(zip, BuildConfig.API_KEY, "5")

}