package com.example.pokmons.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokmons.data.serializables.Stats

@Entity(tableName = "pokemons")
data class Pokemon(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "pokemonId") val pokemonId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
    @ColumnInfo(name = "stats") val stats: List<Stats>,
    )