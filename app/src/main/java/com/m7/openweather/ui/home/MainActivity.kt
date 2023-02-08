package com.m7.openweather.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.m7.openweather.R
import com.m7.openweather.databinding.ActivityMainBinding
import com.m7.openweather.ui.base.BaseActivity
import com.m7.openweather.ui.forecast.ForecastActivity
import com.m7.openweather.ui.weather.WeatherActivity
import com.m7.openweather.ui.weather.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val weatherViewModel by viewModels<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        weatherViewModel.getLastWeather()
        layoutBinding.apply {

            observe(weatherViewModel.dataState) {
                it?.also {
                    forecast = it
                }
            }

            cvCurrentWeather.setOnClickListener {
                startActivity(Intent(this@MainActivity, WeatherActivity::class.java))
            }

            cvForecast.setOnClickListener {
                startActivity(Intent(this@MainActivity, ForecastActivity::class.java))
            }
        }
    }
}