package com.m7.openweather.data.remote

import com.m7.openweather.data.model.CallResponse
import com.m7.openweather.data.model.Forecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIs {

    @GET("forecast")
    suspend fun forecast(
        @Query("lat") lat: String,
        @Query("lon") lng: String,
        @Query("appid") apiKey: String,
    ): Response<CallResponse<Forecast>>
}