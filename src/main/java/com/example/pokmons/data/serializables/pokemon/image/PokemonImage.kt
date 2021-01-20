package com.example.pokmons.data.serializables.pokemon.image

import kotlinx.serialization.Serializable

@Serializable
data class PokemonImage(
    val sprites: Sprites
)