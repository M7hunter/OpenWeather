package com.m7.openweather.data.model

data class SearchTitle(
    val name: String,
    override val type: SearchType = SearchType.SearchTitle
) : Search()
