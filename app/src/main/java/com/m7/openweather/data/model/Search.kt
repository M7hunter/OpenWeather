package com.m7.openweather.data.model

abstract class Search {
    abstract val type: SearchType
}

enum class SearchType {
    SearchTitle,
    SearchOption,
    SearchResult
}
