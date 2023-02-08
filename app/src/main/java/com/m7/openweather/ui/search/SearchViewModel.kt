package com.m7.openweather.ui.search

import androidx.lifecycle.ViewModel
import com.m7.openweather.R
import com.m7.openweather.data.model.*
import com.m7.openweather.data.model.Option.TempUnit
import kotlinx.coroutines.flow.StateFlow

abstract class SearchViewModel
    : ViewModel() {

    abstract val dataState: StateFlow<CallState<CallResponse>?>

    abstract fun getByLatLng(lat: String, lng: String, unit: TempUnit? = null)

    abstract fun getByCityName(cityName: String)

    abstract fun getByZip(zip: String)

    abstract fun getSearchList(): List<Search>

    val options = listOf(
        SearchTitle(R.string.search_options),
        SearchOption(R.string.city_name),
        SearchOption(R.string.zip_code, OptionType.Zip),
        SearchOption(R.string.lat_lng, OptionType.LatLng),
        SearchOption(R.string.current_location, OptionType.CurrentLocation)
    )
}