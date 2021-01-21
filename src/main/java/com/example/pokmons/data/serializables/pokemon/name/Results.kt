package com.example.pokmons.data.serializables.pokemon.name

import kotlinx.serialization.Serializable

@Serializable
data class Results(
        val name: String,
        val url: String
        )