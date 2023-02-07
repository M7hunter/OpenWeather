package com.m7.openweather.ui.forecast

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.m7.openweather.R
import com.m7.openweather.data.model.Forecast
import com.m7.openweather.data.model.ForecastResponse
import com.m7.openweather.data.model.SearchResult
import com.m7.openweather.databinding.ActivityForecastBinding
import com.m7.openweather.ui.base.BaseActivity
import com.m7.openweather.ui.search.SearchActivity
import com.m7.openweather.ui.search.SearchActivity.Companion.KEY_SEARCH_RECEIVER
import com.m7.openweather.ui.search.SearchActivity.Companion.KEY_WEATHER_DATA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForecastActivity : BaseActivity<ActivityForecastBinding>(R.layout.activity_forecast) {

    private val forecastViewModel by viewModels<ForecastViewModel>()

    private var currentSearchResultFlow = MutableStateFlow<SearchResult?>(null)

    companion object {
        const val FORECAST = "forecast"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            currentSearchResultFlow.emit(
                getLocationFromExtras(intent.extras)
                    ?: run {
                        sharedPrefs.getSearchLocations()?.lastOrNull()
                    }
            )
        }

        layoutBinding.apply {

            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            val searchActivityLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == RESULT_OK) {
                    getDataFromExtras(it.data?.extras)?.also {
                        setupRecyclerview(it.list)
                        currentSearchResultFlow.value = SearchResult(it.city.coord.lat, it.city.coord.lon)
                    }
                }
            }

            btnSearch.setOnClickListener {
                searchActivityLauncher.launch(Intent(
                    this@ForecastActivity,
                    SearchActivity::class.java
                )
                    .also { KEY_SEARCH_RECEIVER = FORECAST })
            }

            lifecycleScope.launch {
                observe(forecastViewModel.dataState) {
                    it?.also {
                        setupRecyclerview(it.list)
                    }
                }

                currentSearchResultFlow.collectLatest {
                    it?.also {
                        forecastViewModel.getByLatLng(it.lat, it.lng)
                    }
                }
            }
        }
    }

    private fun setupRecyclerview(list: List<Forecast>) {
        layoutBinding.rvForecasts.apply {
            layoutManager = LinearLayoutManager(this@ForecastActivity)
            adapter = ForecastAdapter(list)
        }
    }

    private fun getLocationFromExtras(bundle: Bundle?): SearchResult? {
        return bundle?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(
                    SearchActivity.KEY_LOCATION,
                    SearchResult::class.java
                )
            } else {
                it.getParcelable(SearchActivity.KEY_LOCATION)
            }
        }
    }

    private fun getDataFromExtras(bundle: Bundle?): ForecastResponse? {
        return bundle?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(
                    KEY_WEATHER_DATA,
                    ForecastResponse::class.java
                )
            } else {
                it.getParcelable(KEY_WEATHER_DATA)
            }
        }
    }
}