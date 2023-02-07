package com.m7.openweather.data.source.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.google.gson.Gson
import com.m7.openweather.data.model.SearchResult
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefManager @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        const val KEY_PREFS = "OpenWeatherPrefs"
        const val KEY_SEARCH_LOCATIONS = "searchLocations"
    }

    private val sp = context.getSharedPreferences(KEY_PREFS, MODE_PRIVATE)
    private val editor = sp.edit()
    private val gson = Gson()

    fun <T> save(key: String, value: T) {
        editor.putString(key, gson.toJson(value)).commit()
    }

    fun delete(key: String) {
        editor.remove(key).commit()
    }

    fun <T> get(key: String, type: T): T? {
        return gson.fromJson(sp.getString(key, null), type!!::class.java)
    }

    fun updateSearchLocations(searchResult: SearchResult) {
        sp.getStringSet(KEY_SEARCH_LOCATIONS, null)
            ?.map { gson.fromJson(it, SearchResult::class.java) }
            ?.toMutableList()?.apply {
                if (!removeIf { it.lat == searchResult.lat && it.lng == searchResult.lng } && size >= 10)
                    removeAt(lastIndex)

                add(searchResult)

                editor.putStringSet(KEY_SEARCH_LOCATIONS, map { gson.toJson(it) }.toSet()).commit()
            }
            ?: editor.putStringSet(KEY_SEARCH_LOCATIONS, setOf(gson.toJson(searchResult))).commit()
    }

    fun getSearchLocations(): List<SearchResult>? {
        return sp.getStringSet(KEY_SEARCH_LOCATIONS, null)
            ?.map { gson.fromJson(it, SearchResult::class.java) }
    }
}