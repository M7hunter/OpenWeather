package com.m7.openweather.ui.forecast

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.m7.openweather.data.model.CallState
import com.m7.openweather.data.model.ForecastResponse
import com.m7.openweather.data.model.Option.DailyFilter
import com.m7.openweather.data.model.Option.TempUnit
import com.m7.openweather.data.repo.ForecastRepo
import com.m7.openweather.ui.search.SearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val forecastRepo: ForecastRepo
) : SearchViewModel() {

    private val _forecastData = MutableStateFlow<CallState<ForecastResponse>?>(null)
    override val dataState = _forecastData.asStateFlow()

    fun getForecastDailyByLatLng(lat: String, lng: String, dailyFilter: DailyFilter) {
        viewModelScope.launch {
            forecastRepo.getForecastDailyByLatLng(_forecastData, lat, lng, dailyFilter)
        }
    }

    override fun getByLatLng(lat: String, lng: String, unit: TempUnit?) {
        viewModelScope.launch {
            forecastRepo.getForecastByLatLng(_forecastData, lat, lng)
        }
    }

    override fun getByCityName(cityName: String) {
        viewModelScope.launch {
            forecastRepo.getForecastByCityName(_forecastData, cityName)
        }
    }

    override fun getByZip(zip: String) {
        viewModelScope.launch {
            forecastRepo.getForecastByZip(_forecastData, zip)
        }
    }

    fun getPassedOrLastForecast(extras: Bundle?) {
        viewModelScope.launch {
            forecastRepo.getPassedOrLastForecast(_forecastData, extras)
        }
    }

    override fun getSearchList() =
        options.toMutableList().apply { addAll(forecastRepo.getSearchHistory()) }
}