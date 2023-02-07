package com.m7.openweather.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResult(
    val lat: String,
    val lng: String,
    val name: String? = null,
    override val type: SearchType = SearchType.SearchResult,
) : Search(), Parcelable
