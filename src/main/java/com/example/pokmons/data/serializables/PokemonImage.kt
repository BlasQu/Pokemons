package com.example.pokmons.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class PokemonImage(
    val sprites: Sprites
)