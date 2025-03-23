package com.example.events.utils

sealed class Resource<T>(val data:T? = null, val error: Exception? = null) {
    class Success<T>(data:T?):Resource<T>(data)
    class Error<T>(error: Exception): Resource<T>(error = error)
}