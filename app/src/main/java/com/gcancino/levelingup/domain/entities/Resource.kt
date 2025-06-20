package com.gcancino.levelingup.domain.entities

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val exception: Exception? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null, exception: Exception? = null) : Resource<T>(data, message, exception)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}