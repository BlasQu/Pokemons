package com.example.pokmons.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val name: String,
    val url: String
)