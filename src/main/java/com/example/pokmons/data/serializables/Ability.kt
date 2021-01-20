package com.example.pokmons.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class Ability(
    val ability: AbilityName,
)