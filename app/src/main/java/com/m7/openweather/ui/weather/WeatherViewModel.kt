package com.m7.openweather.ui.weather

import androidx.lifecycle.viewModelScope
import com.m7.openweather.data.model.CallState
import com.m7.openweather.data.model.WeatherResponse
import com.m7.openweather.data.repo.WeatherRepo
import com.m7.openweather.ui.search.SearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepo: WeatherRepo
) : SearchViewModel() {

    private val _weatherData = MutableStateFlow<CallState<WeatherResponse>?>(null)
    override val dataState = _weatherData.asStateFlow()

    override fun getByLatLng(lat: String, lng: String) {
        viewModelScope.launch {
            weatherRepo.getWeatherByLatLng(_weatherData, lat, lng)
        }
    }

    override fun getByCityName(cityName: String) {
        viewModelScope.launch {
            weatherRepo.getWeatherByCityName(_weatherData, cityName)
        }
    }

    override fun getByZip(zip: String) {
        viewModelScope.launch {
            weatherRepo.getWeatherByZip(_weatherData, zip)
        }
    }
}