package com.m7.openweather.ui.forecast

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.m7.openweather.R
import com.m7.openweather.data.model.Option.DailyFilter
import com.m7.openweather.data.model.SearchResult
import com.m7.openweather.databinding.ActivityForecastBinding
import com.m7.openweather.ui.base.BaseActivity
import com.m7.openweather.ui.base.OptionsFragment
import com.m7.openweather.ui.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForecastActivity : BaseActivity<ActivityForecastBinding>(R.layout.activity_forecast) {

    private val forecastViewModel by viewModels<ForecastViewModel>()

    private lateinit var searchFragment: SearchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchFragment = SearchFragment(forecastViewModel)
        forecastViewModel.getPassedOrLastForecast(intent.extras)

        layoutBinding.apply {
            observe(forecastViewModel.dataState) {
                it?.also {
                    sharedPrefs.updateSearchHistory(
                        SearchResult(
                            it.city.id,
                            it.city.coord.lat,
                            it.city.coord.lon,
                            it.city.name
                        )
                    )
                    supportFragmentManager.popBackStack()

                    locationName = it.city.name
                    rvForecasts.apply {
                        layoutManager = LinearLayoutManager(this@ForecastActivity)
                        adapter = ForecastAdapter(it.list)
                    }
                }
            }

            btnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }


            val filterSheet = OptionsFragment(listOf(
                DailyFilter.OneDay,
                DailyFilter.TwoDays
            ),
                onOptionSelected = { option ->
                    forecastViewModel.dataState.value?.data?.city?.also {
                        forecastViewModel.getForecastDailyByLatLng(
                            it.coord.lat,
                            it.coord.lon,
                            option as DailyFilter
                        )
                    }
                }
            )

            btnFilter.setOnClickListener {
                filterSheet.apply {
                    if (!isAdded) show(supportFragmentManager, tag)
                }
            }

            btnSearch.setOnClickListener {
                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                        android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right
                    )
                    .replace(R.id.cl_forecast_container, searchFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}