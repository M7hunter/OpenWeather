package com.m7.openweather.data.repo

import com.m7.openweather.data.model.CallState
import com.m7.openweather.data.model.ForecastResponse
import com.m7.openweather.util.ConnectivityHandler
import com.m7.openweather.data.source.remote.APIsImpl
import com.m7.openweather.data.source.remote.CallManager
import com.m7.openweather.data.source.remote.CallManagerImpl
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ForecastRepo @Inject constructor(
    connectivityHandler: ConnectivityHandler,
    private val apIsImpl: APIsImpl
) :
    CallManager by CallManagerImpl(connectivityHandler) {

    suspend fun getForecastByLatLng(
        _forecastData: MutableStateFlow<CallState<ForecastResponse>?>,
        lat: String,
        lng: String
    ) =
        call(_forecastData) { apIsImpl.getForecastByLatLng(lat, lng) }

    suspend fun getForecastByCityName(
        _forecastData: MutableStateFlow<CallState<ForecastResponse>?>,
        cityName: String
    ) =
        call(_forecastData) { apIsImpl.getForecastByCityName(cityName) }

    suspend fun getForecastByZip(
        _forecastData: MutableStateFlow<CallState<ForecastResponse>?>,
        zip: String
    ) =
        call(_forecastData) { apIsImpl.getForecastByZip(zip) }
}