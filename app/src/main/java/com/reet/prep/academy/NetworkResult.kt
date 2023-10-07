package com.reet.prep.academy

sealed class NetworkResult<T>(data: T? = null, message: String? = null) {
    class Success<T>(val data: T?) : NetworkResult<T>(data)
    class Loading<T> : NetworkResult<T>()
    class Failure<T>(val message: String?) : NetworkResult<T>(message = message)
}