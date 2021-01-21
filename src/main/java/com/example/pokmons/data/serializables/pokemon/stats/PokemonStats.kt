package com.example.pokmons.data.serializables.pokemon.stats

import com.example.pokmons.data.serializables.pokemon.type.Type
import kotlinx.serialization.Serializable

@Serializable
data class PokemonStats(
    val abilities: List<Ability>,
    val height: Int,
    val weight: Int,
    val types: List<Type>,
    val forms: List<Forms>
)