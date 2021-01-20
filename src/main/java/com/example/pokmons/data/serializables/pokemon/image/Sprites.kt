package com.example.pokmons.data.serializables.pokemon.image

import kotlinx.serialization.Serializable

@Serializable
data class Sprites(
    val back_default: String,
    val back_shiny: String,
    val front_default: String,
    val front_shiny: String
)