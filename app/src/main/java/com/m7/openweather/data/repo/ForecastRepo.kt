package com.m7.openweather.data.repo

import android.os.Build
import android.os.Bundle
import com.m7.openweather.R
import com.m7.openweather.data.model.*
import com.m7.openweather.data.model.Option.DailyFilter
import com.m7.openweather.data.source.local.SharedPrefManager
import com.m7.openweather.util.ConnectivityHandler
import com.m7.openweather.data.source.remote.APIsImpl
import com.m7.openweather.data.source.remote.CallManager
import com.m7.openweather.data.source.remote.CallManagerImpl
import com.m7.openweather.ui.weather.WeatherActivity.Companion.KEY_LOCATION
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ForecastRepo @Inject constructor(
    connectivityHandler: ConnectivityHandler,
    private val apIsImpl: APIsImpl,
    private val sharedPrefs: SharedPrefManager
) :
    CallManager by CallManagerImpl(connectivityHandler) {

    suspend fun getForecastDailyByLatLng(
        _forecastData: MutableStateFlow<CallState<ForecastResponse>?>,
        lat: String,
        lng: String,
        dailyFilter: DailyFilter
    ) =
        call(_forecastData) { apIsImpl.getForecastDailyByLatLng(lat, lng, dailyFilter) }

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

    suspend fun getPassedOrLastForecast(
        _forecastData: MutableStateFlow<CallState<ForecastResponse>?>,
        extras: Bundle?
    ) {
        val result =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                extras?.getParcelable(KEY_LOCATION, SearchResult::class.java)
            } else {
                extras?.getParcelable(KEY_LOCATION)
            }
                ?: sharedPrefs.let {
                    it.getSearchHistory()?.lastOrNull()
                }


        result?.also {
            getForecastByLatLng(_forecastData, it.lat, it.lng)
        }
    }

    fun getSearchHistory() =
        mutableListOf<Search>().apply {
            sharedPrefs.getSearchHistory()
                ?.takeIf { it.isNotEmpty() }
                ?.asReversed()
                ?.also {
                    add(SearchTitle(R.string.search_history))
                    addAll(if (it.size > 5) it.subList(0, 4) else it)
                }
        }
}