package com.example.pokmons.util

sealed class RequestState {
    object EMPTY: RequestState()
    object LOADING: RequestState()
    object SUCCESS: RequestState()
    data class ERROR(val message: String): RequestState()
}