package com.example.pokmons.data.serializables.pokemon.stats

import kotlinx.serialization.Serializable

@Serializable
data class PokemonStats(
    val abilities: List<Ability>,
    val height: Int,
    val weight: Int
)