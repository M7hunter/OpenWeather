package com.m7.openweather.data.source.local

import android.content.Context
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.m7.openweather.data.model.WeatherResponse
import com.m7.openweather.data.source.local.SharedPrefManager.Companion.KEY_SEARCHED_LOCATIONS

class SavedLocationLifespan(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {

    private val sp = context.getSharedPreferences(SharedPrefManager.KEY_PREFS, Context.MODE_PRIVATE)
    private val editor = sp.edit()
    private val gson = Gson()

    override fun doWork(): Result {
        return removeSavedLocation(inputData.getLong(SharedPrefManager.KEY_REMOVE_LOCATION_ID, -1L))
    }

    private fun removeSavedLocation(id: Long): Result {
        if (id != -1L) {
            sp.getStringSet(KEY_SEARCHED_LOCATIONS, null)
                ?.map { gson.fromJson(it, WeatherResponse::class.java) }
                ?.toMutableList()?.apply {
                    if (!removeIf { it.id == id }) return Result.failure()

                    editor
                        .remove(KEY_SEARCHED_LOCATIONS)
                        .putStringSet(KEY_SEARCHED_LOCATIONS, map { gson.toJson(it) }.toSet())
                        .commit()

                    Toast.makeText(context, "location $id removed", Toast.LENGTH_LONG).show()
                    return Result.success()
                }
        }

        return Result.failure()
    }
}