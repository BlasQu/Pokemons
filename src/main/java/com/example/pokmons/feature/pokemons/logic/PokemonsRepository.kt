package com.example.pokmons.feature.pokemons.logic

import com.example.pokmons.data.api.PokemonsService
import com.example.pokmons.data.entities.Pokemon
import com.example.pokmons.data.room.PokemonsDao
import com.example.pokmons.data.serializables.pokemon.name.Results
import com.example.pokmons.data.serializables.Stats
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonsRepository @Inject constructor(
    val dao: PokemonsDao,
    val api: PokemonsService
) {

    suspend fun insertPokemons(pokemons: List<Pokemon>) = dao.insertPokemons(pokemons)

    fun readPokemons(): Flow<List<Pokemon>> = dao.readPokemons()

    suspend fun deleteAllData() = dao.deleteAllData()

    suspend fun responseGetPokemons(startPoint: Int): List<Results> {
        val resultsList = mutableListOf<Results>()
        val response = api.getPokemon(50, startPoint)
        if (response.isSuccessful) {
            val results = response.body()!!.results
            for (every in results) {
                resultsList.add(Results(every.name))
            }
        }
        return resultsList
    }

    fun responseGetPokemonsImage(startPoint: Int): List<String> {
        val imagesUrlList = mutableListOf<String>()

        for (number in startPoint+1..startPoint+50) {
            val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
            imagesUrlList.add(url)
        }

        return imagesUrlList
    }

    suspend fun responseGetPokemonsAbilities(startPoint: Int): List<Stats> {
        val abilities = mutableListOf<String>()
        val pokemonsStats = mutableListOf<Stats>()
        var pokemonWeight = 0
        var pokemonHeight = 0

        for (number in startPoint+1..startPoint+50) {
            val url = "https://pokeapi.co/api/v2/pokemon/${number}/"
            val response = api.getAbilities(url)
            if (response.isSuccessful) {
                val body = response.body()!!
                pokemonWeight = body.weight
                pokemonHeight = body.height
                for (every in body.abilities) {
                    abilities.add(every.ability.toString())
                }
                val stats = Stats(abilities, pokemonWeight, pokemonHeight)
                pokemonsStats.add(stats)
            }
        }

        return pokemonsStats
    }
}