package com.m7.openweather.data.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

abstract class Search {
    abstract val type: SearchType
}

enum class SearchType {
    SearchTitle,
    SearchOption,
    SearchResult
}

@Parcelize
data class SearchResult(
    val id: Long,
    val lat: String,
    val lng: String,
    val name: String? = null,
    override val type: SearchType = SearchType.SearchResult,
) : Search(), Parcelable

data class SearchTitle(
    @StringRes val name: Int,
    override val type: SearchType = SearchType.SearchTitle
) : Search()