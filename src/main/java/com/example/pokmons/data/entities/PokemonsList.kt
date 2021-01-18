package com.example.pokmons.data.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class PokemonsList(
    val results: List<Result>
)