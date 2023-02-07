package com.m7.openweather.data.source.remote

import com.m7.openweather.data.model.ForecastResponse
import com.m7.openweather.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIs {

    @GET("weather")
    suspend fun weatherByLatLng(
        @Query("lat") lat: String,
        @Query("lon") lng: String,
        @Query("appid") apiKey: String,
    ): Response<WeatherResponse>

    @GET("weather")
    suspend fun weatherByCityName(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
    ): Response<WeatherResponse>

    @GET("weather")
    suspend fun weatherByZip(
        @Query("zip") zip: String,
        @Query("appid") apiKey: String,
    ): Response<WeatherResponse>

    @GET("forecast")
    suspend fun forecastByLatLng(
        @Query("lat") lat: String,
        @Query("lon") lng: String,
        @Query("appid") apiKey: String,
        @Query("cnt") count: String,
    ): Response<ForecastResponse>

    @GET("forecast")
    suspend fun forecastByCityName(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("cnt") count: String,
    ): Response<ForecastResponse>

    @GET("forecast")
    suspend fun forecastByZip(
        @Query("zip") zip: String,
        @Query("appid") apiKey: String,
        @Query("cnt") count: String,
    ): Response<ForecastResponse>
}