package com.example.pokmons.data.serializables

import com.example.pokmons.data.serializables.pokemon.name.Results
import kotlinx.serialization.Serializable

@Serializable
data class PokemonConnector(val pokemonId: Int, val pokemonName: String, val imagesUrl: String, val stats: Stats)