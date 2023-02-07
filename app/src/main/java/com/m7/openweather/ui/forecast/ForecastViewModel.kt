package com.m7.openweather.ui.forecast

import androidx.lifecycle.viewModelScope
import com.m7.openweather.data.model.CallState
import com.m7.openweather.data.model.ForecastResponse
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

    override fun getByLatLng(lat: String, lng: String) {
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
}