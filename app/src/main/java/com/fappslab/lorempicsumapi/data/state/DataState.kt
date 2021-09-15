package com.fappslab.lorempicsumapi.data.state

import com.fappslab.lorempicsumapi.data.model.Error
import retrofit2.Response

sealed class DataState<out R> {
    data class OnSuccess<out T>(val data: T) : DataState<T>()
    data class OnError(val error: Error) : DataState<Nothing>()
    data class OnException(val e: Exception) : DataState<Nothing>()
}

fun <T : Any> Response<T?>?.parseResponse(): DataState<T?> {
    return try {
        when (this) {
            null -> throw NullPointerException("response is null!")
            else -> when (isSuccessful) {
                true -> DataState.OnSuccess(body())
                else -> {
                    val error = Error.getError(errorBody(), code())
                    DataState.OnError(error)
                }
            }
        }
    } catch (e: Exception) {
        DataState.OnException(e)
    }
}
