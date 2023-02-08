package com.m7.openweather.data.source.remote

import com.m7.openweather.BuildConfig
import com.m7.openweather.data.model.Option.DailyFilter
import com.m7.openweather.data.model.Option.TempUnit
import javax.inject.Inject

class APIsImpl @Inject constructor(private val apIs: APIs) {

    suspend fun getWeatherByLatLng(lat: String, lng: String, unit: TempUnit?) =
        apIs.weatherByLatLng(lat, lng, unit?.value, BuildConfig.API_KEY)

    suspend fun getWeatherByCityName(cityName: String) =
        apIs.weatherByCityName(cityName, BuildConfig.API_KEY)

    suspend fun getWeatherByZip(zip: String) =
        apIs.weatherByZip(zip, BuildConfig.API_KEY)

    suspend fun getForecastDailyByLatLng(
        lat: String,
        lng: String,
        dailyFilter: DailyFilter = DailyFilter.OneDay
    ) =
        apIs.forecastDailyByLatLng(lat, lng, BuildConfig.API_KEY, dailyFilter.value)

    suspend fun getForecastByLatLng(lat: String, lng: String) =
        apIs.forecastByLatLng(lat, lng, BuildConfig.API_KEY, "16")

    suspend fun getForecastByCityName(cityName: String) =
        apIs.forecastByCityName(cityName, BuildConfig.API_KEY, "16")

    suspend fun getForecastByZip(zip: String) =
        apIs.forecastByZip(zip, BuildConfig.API_KEY, "16")

}