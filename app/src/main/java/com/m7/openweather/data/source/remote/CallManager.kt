package com.m7.openweather.data.source.remote

import com.m7.openweather.data.model.CallResponse
import com.m7.openweather.util.ConnectivityHandler
import com.m7.openweather.data.model.CallState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import java.net.ConnectException

interface CallManager {

    suspend fun <T : CallResponse> call(
        observable: MutableStateFlow<CallState<T>?>,
        action: suspend () -> Response<T>
    )
}

class CallManagerImpl(
    private val connectivityHandler: ConnectivityHandler
) : CallManager {

    override suspend fun <T : CallResponse> call(
        observable: MutableStateFlow<CallState<T>?>,
        action: suspend () -> Response<T>
    ) {
        try {
            if (connectivityHandler.isConnected()) {
                observable.emit(CallState.loading())

                action.invoke().let {
                    it.body()?.let { mainBody ->
                        if (mainBody.cod.toString() == "200") {
                            observable.emit(CallState.success(mainBody, mainBody.message))
                        } else {
                            observable.emit(CallState.error(NullPointerException("${mainBody.cod}: ${mainBody.message}")))
                        }
                    }
                        ?: observable.emit(CallState.error(NullPointerException("${it.code()}: ${it.message()}")))
                }
            } else {
                observable.emit(CallState.error(ConnectException("no internet connection")))
            }
        } catch (e: Exception) {
            observable.emit(CallState.error(e))
        }
    }
}