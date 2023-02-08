package com.m7.openweather.data.repo

import com.m7.openweather.R
import com.m7.openweather.data.model.*
import com.m7.openweather.data.model.Option.TempUnit
import com.m7.openweather.data.source.local.SharedPrefManager
import com.m7.openweather.util.ConnectivityHandler
import com.m7.openweather.data.source.remote.APIsImpl
import com.m7.openweather.data.source.remote.CallManager
import com.m7.openweather.data.source.remote.CallManagerImpl
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class WeatherRepo @Inject constructor(
    connectivityHandler: ConnectivityHandler,
    private val apIsImpl: APIsImpl,
    private val sharedPrefs: SharedPrefManager
) :
    CallManager by CallManagerImpl(connectivityHandler) {

    suspend fun getWeatherByLatLng(
        _weatherData: MutableStateFlow<CallState<WeatherResponse>?>,
        lat: String,
        lng: String,
        unit: TempUnit?
    ) =
        call(_weatherData) { apIsImpl.getWeatherByLatLng(lat, lng, unit) }


    suspend fun getWeatherByCityName(
        _weatherData: MutableStateFlow<CallState<WeatherResponse>?>,
        cityName: String,
    ) =
        call(_weatherData) { apIsImpl.getWeatherByCityName(cityName) }

    suspend fun getWeatherByZip(
        _weatherData: MutableStateFlow<CallState<WeatherResponse>?>,
        zip: String,
    ) =
        call(_weatherData) { apIsImpl.getWeatherByZip(zip) }

    suspend fun getLastWeather(_weatherData: MutableStateFlow<CallState<WeatherResponse>?>) {
        sharedPrefs.also {
            it.getSearchedLocations()?.lastOrNull()?.apply {
                _weatherData.emit(CallState.success(this, null))
            }
                ?: it.getSearchHistory()?.lastOrNull()?.also {
                    getWeatherByLatLng(_weatherData, it.lat, it.lng, null)
                }
        }
    }

    fun getSearchHistory() =
        mutableListOf<Search>().apply {
            sharedPrefs.getSearchHistory()
                ?.takeIf { it.isNotEmpty() }
                ?.asReversed()
                ?.also {
                    add(SearchTitle(R.string.search_history))
                    addAll(it)
                }
        }
}