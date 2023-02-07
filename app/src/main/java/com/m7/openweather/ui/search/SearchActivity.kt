package com.m7.openweather.ui.search

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.m7.openweather.R
import com.m7.openweather.data.model.*
import com.m7.openweather.databinding.ActivitySearchBinding
import com.m7.openweather.ui.base.BaseActivity
import com.m7.openweather.ui.forecast.ForecastActivity.Companion.FORECAST
import com.m7.openweather.ui.forecast.ForecastViewModel
import com.m7.openweather.ui.weather.WeatherActivity.Companion.WEATHER
import com.m7.openweather.ui.weather.WeatherViewModel
import com.m7.openweather.util.LocationHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {

    private val weatherViewModel by viewModels<WeatherViewModel>()
    private val forecastViewModel by viewModels<ForecastViewModel>()
    private lateinit var searchViewModel: SearchViewModel

    private val locationHandler = LocationHandler(this)

    private var optionType = OptionType.CityName
    private var searchText = ""


    companion object {
        var KEY_SEARCH_RECEIVER = WEATHER
        const val KEY_WEATHER_DATA = "WeatherData"
        const val KEY_LOCATION = "location"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchViewModel =
            if (KEY_SEARCH_RECEIVER == FORECAST) forecastViewModel else weatherViewModel

        layoutBinding.apply {
            etSearch.requestFocus()

            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            etSearch.doOnTextChanged { text, _, _, _ ->
                text?.toString()?.also { searchText = it }
            }

            btnSearch.setOnClickListener {
                if (optionType == OptionType.CurrentLocation) useCurrentLocation()
                else
                    searchText.takeIf { it.isNotBlank() }?.also {
                        when (optionType) {
                            OptionType.CityName -> searchViewModel.getByCityName(it)
                            OptionType.Zip -> searchViewModel.getByZip(it)
                            OptionType.LatLng -> {
                                searchViewModel.getByLatLng(
                                    it.substringBefore("/"),
                                    it.substringAfter("/")
                                )
                            }
                            OptionType.CurrentLocation -> useCurrentLocation()
                        }
                    } ?: run { etSearch.error = getString(R.string.enter_valid_input) }
            }

            observe(searchViewModel.dataState) {
                it?.also {
                    returnWithResult(it)
                }
            }

            val options = mutableListOf(
                SearchTitle(getString(R.string.search_options)),
                SearchOption(getString(R.string.city_name)),
                SearchOption(getString(R.string.zip_code), OptionType.Zip),
                SearchOption(getString(R.string.lat_lng), OptionType.LatLng),
                SearchOption(getString(R.string.current_location), OptionType.CurrentLocation)
            ).apply {
                sharedPrefs.getSearchLocations()?.asReversed()?.takeIf { it.isNotEmpty() }?.also {
                    add(SearchTitle(getString(R.string.search_history)))
                    addAll(it.let {
                        if (KEY_SEARCH_RECEIVER == FORECAST && it.size > 5)
                            it.subList(0, 4)
                        else
                            it
                    })
                }
            }

            rvSearchResults.apply {
                layoutManager = LinearLayoutManager(this@SearchActivity)
                adapter = SearchAdapter(options,
                    onOptionClicked = {
                        optionType = it.optionType
                        etSearch.hint = getString(
                            R.string.search_hint,
                            getString(it.optionType.nameRes)
                        )
                        etSearch.requestFocus()
                    },
                    onLocationClicked = {
                        searchViewModel.getByLatLng(it.lat, it.lng)
                    })
            }
        }
    }

    private fun useCurrentLocation() {
        locationHandler.getLocation()?.also {
            Log.d("M_7_", "useCurrentLocation: $it")
            searchViewModel.getByLatLng(it.latitude.toString(), it.longitude.toString())
        }
            ?: ActivityCompat.requestPermissions(
                this@SearchActivity,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )

    }

    private fun returnWithResult(response: CallResponse) {
        val searchResult = when (KEY_SEARCH_RECEIVER) {
            WEATHER -> {
                (response as WeatherResponse).let {
                    SearchResult(it.coord.lat, it.coord.lon)
                }
            }
            FORECAST -> {
                (response as ForecastResponse).let {
                    SearchResult(
                        it.city.coord.lat,
                        it.city.coord.lon,
                        "${it.city.name},${it.city.country}"
                    )
                }
            }
            else -> {
                null
            }
        }
        searchResult?.also {
            sharedPrefs.updateSearchLocations(it)
        }
        setResult(RESULT_OK, Intent().putExtra(KEY_WEATHER_DATA, response))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.any { it == PackageManager.PERMISSION_DENIED }) {

        }
    }
}