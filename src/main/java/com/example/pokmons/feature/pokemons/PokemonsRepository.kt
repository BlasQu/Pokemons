package com.example.pokmons.feature.pokemons

import com.example.pokmons.data.entities.Pokemon
import com.example.pokmons.data.room.PokemonsDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonsRepository @Inject constructor(
    val dao: PokemonsDao
) {

    suspend fun insertPokemons(pokemons: List<Pokemon>) = dao.insertPokemons(pokemons)


    fun readPokemons(): Flow<List<Pokemon>> = dao.readPokemons()

    suspend fun deleteAllData() = dao.deleteAllData()
}