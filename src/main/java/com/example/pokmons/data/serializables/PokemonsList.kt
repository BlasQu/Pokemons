package com.example.pokmons.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class PokemonsList(
    val results: List<Result>
)