package com.m7.openweather.ui.weather

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.m7.openweather.R
import com.m7.openweather.data.model.Option.TempUnit
import com.m7.openweather.data.model.SearchResult
import com.m7.openweather.databinding.ActivityWeatherBinding
import com.m7.openweather.ui.base.BaseActivity
import com.m7.openweather.ui.base.OptionsFragment
import com.m7.openweather.ui.forecast.ForecastActivity
import com.m7.openweather.ui.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : BaseActivity<ActivityWeatherBinding>(R.layout.activity_weather) {

    private val weatherViewModel by viewModels<WeatherViewModel>()

    private lateinit var searchFragment: SearchFragment
    private var tempUnit: TempUnit = TempUnit.Standard

    companion object {
        const val KEY_LOCATION = "location"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchFragment = SearchFragment(weatherViewModel)
        weatherViewModel.getLastWeather()

        layoutBinding.apply {
            observe(weatherViewModel.dataState) {
                it?.also {
                    sharedPrefs.updateSearchedLocations(it)
                    supportFragmentManager.popBackStack()

                    it.main.unit = tempUnit.short
                    weather = it
                }
            }

            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            btnSearch.setOnClickListener {
                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                    )
                    .replace(R.id.cl_weather_container, searchFragment)
                    .addToBackStack(null)
                    .commit()
            }

            val unitsSheet = OptionsFragment(listOf(
                TempUnit.Standard,
                TempUnit.Imperial,
                TempUnit.Metric
            ),
                onOptionSelected = { selectedTempUnit ->
                    tempUnit = selectedTempUnit as TempUnit
                    weather?.also {
                        weatherViewModel.getByLatLng(it.coord.lat, it.coord.lon, tempUnit)
                    }
                }
            )

            tvTemp.setOnClickListener {
                unitsSheet.apply {
                    if (!isAdded) show(supportFragmentManager, tag)
                }
            }

            fabForecast.setOnClickListener {
                weatherViewModel.dataState.value?.data?.also {
                    startActivity(
                        Intent(this@WeatherActivity, ForecastActivity::class.java)
                            .putExtra(
                                KEY_LOCATION,
                                SearchResult(it.id, it.coord.lat, it.coord.lon)
                            )
                    )
                } ?: showError("no location provided")
            }
        }
    }
}