package com.m7.openweather.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

abstract class CallResponse : Parcelable {
    abstract val cod: Any
    abstract val message: String?
}

@Parcelize
class WeatherResponse(
    override val cod: Int,
    override val message: String? = "no msg provided",
    val id: Long,
    val name: String,
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val coord: Coord
) : CallResponse() {
    val weatherItem get() = weather.firstOrNull()
}

@Parcelize
class ForecastResponse(
    override val cod: String,
    override val message: String?,
    val list: List<Forecast>,
    val cnt: Int,
    val city: City,
) : CallResponse()