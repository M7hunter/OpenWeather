package com.m7.openweather.ui.weather

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.m7.openweather.R
import com.m7.openweather.data.model.SearchResult
import com.m7.openweather.data.model.WeatherResponse
import com.m7.openweather.databinding.ActivityWeatherBinding
import com.m7.openweather.ui.base.BaseActivity
import com.m7.openweather.ui.forecast.ForecastActivity
import com.m7.openweather.ui.search.SearchActivity
import com.m7.openweather.ui.search.SearchActivity.Companion.KEY_LOCATION
import com.m7.openweather.ui.search.SearchActivity.Companion.KEY_SEARCH_RECEIVER
import com.m7.openweather.ui.search.SearchActivity.Companion.KEY_WEATHER_DATA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherActivity : BaseActivity<ActivityWeatherBinding>(R.layout.activity_weather) {

    private val weatherViewModel by viewModels<WeatherViewModel>()

    private var currentSearchResultFlow = MutableStateFlow<SearchResult?>(null)

    companion object {
        const val WEATHER = "weather"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBinding.apply {
            lifecycleScope.launch {
                currentSearchResultFlow.emit(sharedPrefs.getSearchLocations()?.lastOrNull())
            }

            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            var isCelsius = false
            tvTemp.setOnClickListener {
                forecast?.also {
                    tvTemp.text =
                        if (isCelsius) {
                            getString(R.string.f_fahrenheit, it.main.temp)
                        } else {
                            getString(R.string.f_celsius, it.main.tempAsCelsius())
                        }

                    isCelsius = !isCelsius
                }
            }

            fabForecast.setOnClickListener {
                currentSearchResultFlow.value?.also {
                    startActivity(
                        Intent(this@WeatherActivity, ForecastActivity::class.java)
                            .putExtra(KEY_LOCATION, it)
                    )
                } ?: showError("no location provided")
            }

            val searchActivityLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == RESULT_OK) {
                    getDataFromExtras(it.data?.extras)?.also {
                        forecast = it
                        currentSearchResultFlow.value = SearchResult(it.coord.lat, it.coord.lon)
                    }
                }
            }

            btnSearch.setOnClickListener {
                searchActivityLauncher.launch(Intent(
                    this@WeatherActivity,
                    SearchActivity::class.java
                )
                    .also { KEY_SEARCH_RECEIVER = WEATHER })
            }

            lifecycleScope.launch {
                observe(weatherViewModel.dataState) {
                    it?.also {
                        forecast = it
                    }
                }

                currentSearchResultFlow.collectLatest {
                    it?.also {
                        weatherViewModel.getByLatLng(it.lat, it.lng)
                    }
                }
            }
        }
    }

    private fun getDataFromExtras(bundle: Bundle?): WeatherResponse? {
        return bundle?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(
                    KEY_WEATHER_DATA,
                    WeatherResponse::class.java
                )
            } else {
                it.getParcelable(KEY_WEATHER_DATA)
            }
        }
    }
}