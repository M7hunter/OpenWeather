package com.m7.openweather.data.repo

import com.m7.openweather.data.model.CallState
import com.m7.openweather.data.model.ForecastResponse
import com.m7.openweather.data.model.WeatherResponse
import com.m7.openweather.util.ConnectivityHandler
import com.m7.openweather.data.source.remote.APIsImpl
import com.m7.openweather.data.source.remote.CallManager
import com.m7.openweather.data.source.remote.CallManagerImpl
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class WeatherRepo @Inject constructor(
    connectivityHandler: ConnectivityHandler,
    private val apIsImpl: APIsImpl
) :
    CallManager by CallManagerImpl(connectivityHandler) {

    suspend fun getWeatherByLatLng(
        _weatherData: MutableStateFlow<CallState<WeatherResponse>?>,
        lat: String,
        lng: String
    ) =
        call(_weatherData) { apIsImpl.getWeatherByLatLng(lat, lng) }


    suspend fun getWeatherByCityName(
        _forecastData: MutableStateFlow<CallState<WeatherResponse>?>,
        cityName: String
    ) =
        call(_forecastData) { apIsImpl.getWeatherByCityName(cityName) }

    suspend fun getWeatherByZip(
        _forecastData: MutableStateFlow<CallState<WeatherResponse>?>,
        zip: String
    ) =
        call(_forecastData) { apIsImpl.getWeatherByZip(zip) }
}