package com.m7.openweather.ui.search

import androidx.lifecycle.ViewModel
import com.m7.openweather.data.model.CallResponse
import com.m7.openweather.data.model.CallState
import kotlinx.coroutines.flow.StateFlow

abstract class SearchViewModel
 : ViewModel() {

    abstract val dataState : StateFlow<CallState<CallResponse>?>

    abstract fun getByLatLng(lat: String, lng: String)

    abstract fun getByCityName(cityName: String)

    abstract fun getByZip(zip: String)
}