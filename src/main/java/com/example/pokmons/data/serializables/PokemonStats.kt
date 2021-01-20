package com.example.pokmons.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class PokemonStats(
    val abilities: List<Ability>,
    val height: Int,
    val weight: Int
)