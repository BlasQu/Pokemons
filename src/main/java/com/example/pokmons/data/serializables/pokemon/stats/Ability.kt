package com.example.pokmons.data.serializables.pokemon.stats

import kotlinx.serialization.Serializable

@Serializable
data class Ability(
    val ability: AbilityName,
)