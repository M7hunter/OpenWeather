package com.m7.openweather.data.model

sealed class Option(open val title: String) {

    sealed class TempUnit(override val title: String, val value: String, val short: Char) : Option(title) {
        object Standard : TempUnit("Kelvin", "standard" , 'K')
        object Imperial : TempUnit("Fahrenheit", "imperial", 'F')
        object Metric : TempUnit("Celsius", "metric", 'C')
    }

    sealed class DailyFilter(override val title: String, val value: String) : Option(title) {
        object OneDay : DailyFilter("One Day", "1")
        object TwoDays : DailyFilter("Two Days", "2")
    }
}