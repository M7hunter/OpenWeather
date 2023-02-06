package com.m7.openweather

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class ConnectivityHandler constructor(private val context: Context) {

    fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCaps = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork ?: return false
        ) ?: return false

        return when {
            networkCaps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCaps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCaps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

            else -> false
        }
    }
}