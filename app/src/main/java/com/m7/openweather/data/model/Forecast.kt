package com.m7.openweather.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Forecast(
    val dt: Long,
    val dt_txt: String,
    val main: Main,
    val temp: Temp,
    val weather: List<Weather>,
) : Parcelable {
    val weatherItem get() = weather.firstOrNull()
    val date get() = dt_txt?.substringBefore(" ")
    val time get() = dt_txt?.substringAfter(" ")
}

@Parcelize
data class Main(
    val temp: Float,
    val feels_like: Float,
) : Parcelable {

    var unit = 'K'
    val tempAsString get() = String.format("%.02f $unit", temp)
}

@Parcelize
data class Temp(
    val morn: Float,
    val day: Float,
    val eve: Float,
    val night: Float

) : Parcelable {

    val unit = 'K'
    val tempAsString
        get() = String.format(
            "- morn %.02f\n- day %.02f\n- eve %.02f\n- night %.02f $unit",
            morn,
            day,
            eve,
            night
        )
}

@Parcelize
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
) : Parcelable

@Parcelize
data class City(
    val id: Long,
    val name: String,
    val country: String,
    val coord: Coord,
) : Parcelable

@Parcelize
data class Coord(
    val lat: String,
    val lon: String,
) : Parcelable
