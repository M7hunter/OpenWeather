package com.m7.openweather.data.model

class CallResponse<T>(
    val cod: String,
    val message: String,
    val cnt: Int?,
    val list: List<T>?,
    val city: City?,
)