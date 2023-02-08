package com.m7.openweather.data.source.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.gson.Gson
import com.m7.openweather.data.model.SearchResult
import com.m7.openweather.data.model.WeatherResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefManager @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        const val KEY_PREFS = "OpenWeatherPrefs"
        const val KEY_SEARCH_HISTORY = "searchHistory"
        const val KEY_SEARCHED_LOCATIONS = "searchedLocations"
        const val KEY_REMOVE_LOCATION_ID = "locationToBeRemoved"
    }

    private val sp = context.getSharedPreferences(KEY_PREFS, MODE_PRIVATE)
    private val editor = sp.edit()
    private val gson = Gson()

    fun updateSearchedLocations(location: WeatherResponse) {
        sp.getStringSet(KEY_SEARCHED_LOCATIONS, null)
            ?.map { gson.fromJson(it, WeatherResponse::class.java) }
            ?.toMutableList()?.apply {
                if (!removeIf { it.id == location.id } && size >= 5)
                    removeFirst()

                add(location)

                editor
                    .remove(KEY_SEARCHED_LOCATIONS)
                    .putStringSet(KEY_SEARCHED_LOCATIONS, map { gson.toJson(it) }.toSet())
                    .commit()
            }
            ?: editor
                .putStringSet(KEY_SEARCHED_LOCATIONS, setOf(gson.toJson(location)))
                .commit()

        updateSearchHistory(
            SearchResult(
                location.id,
                location.coord.lat,
                location.coord.lon,
                location.name
            )
        )
        removeLocationAfterLifespan(location.id)
    }

    private fun removeLocationAfterLifespan(id: Long) {
        WorkManager.getInstance(context).enqueue(
            OneTimeWorkRequest.Builder(SavedLocationLifespan::class.java)
                .setInputData(
                    Data.Builder()
                        .putLong(KEY_REMOVE_LOCATION_ID, id)
                        .build()
                )
                .setInitialDelay(5, TimeUnit.MINUTES)
                .build()
        )
    }

    fun getSearchedLocations(): List<WeatherResponse>? {
        return sp.getStringSet(KEY_SEARCHED_LOCATIONS, null)
            ?.map { gson.fromJson(it, WeatherResponse::class.java) }
    }

    fun updateSearchHistory(searchResult: SearchResult) {
        sp.getStringSet(KEY_SEARCH_HISTORY, null)
            ?.map { gson.fromJson(it, SearchResult::class.java) }
            ?.toMutableList()?.apply {
                if (!removeIf { it.lat == searchResult.lat && it.lng == searchResult.lng } && size >= 10)
                    removeAt(0)

                add(searchResult)

                editor
                    .remove(KEY_SEARCH_HISTORY)
                    .putStringSet(KEY_SEARCH_HISTORY, map { gson.toJson(it) }.toSet())
                    .commit()
            }
            ?: editor
                .putStringSet(KEY_SEARCH_HISTORY, setOf(gson.toJson(searchResult)))
                .commit()
    }

    fun getSearchHistory(): List<SearchResult>? {
        return sp.getStringSet(KEY_SEARCH_HISTORY, null)
            ?.map { gson.fromJson(it, SearchResult::class.java) }
    }
}