package com.m7.openweather.ui.base

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.m7.openweather.BuildConfig

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
    url?.also {
        imageView.load(it)
    }
}

@BindingAdapter("iconCode")
fun loadWeatherIcon(imageView: ImageView, iconCode: String?) {
    iconCode?.also {
        imageView.load(BuildConfig.ICON_URL + it + ".png")
    }
}