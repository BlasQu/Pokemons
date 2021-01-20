package com.example.pokmons.data.serializables

import kotlinx.serialization.Serializable

data class Stats(
    val abilities: List<String>,
    val weight: Int,
    val height: Int
)