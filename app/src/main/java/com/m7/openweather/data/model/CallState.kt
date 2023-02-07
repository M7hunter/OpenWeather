package com.m7.openweather.data.model

data class CallState<out T>(
    val status: Status,
    val data: T?,
    val msg: String? = null,
    val ex: Exception? = null,
) {
    enum class Status {
        LOADING,
        SUCCESS,
        ERROR,
    }

    companion object {
        fun loading() = CallState(Status.LOADING, null)
        fun <T> success(data: T, msg: String?) = CallState(Status.SUCCESS, data, msg)
        fun error(ex: Exception?) = CallState(Status.ERROR, null, ex = ex)
    }
}
