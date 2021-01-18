package com.example.pokmons.feature.pokemons

import android.util.Log
import com.example.pokmons.data.api.PokemonsService
import com.example.pokmons.data.entities.Pokemon
import com.example.pokmons.data.room.PokemonsDao
import com.example.pokmons.data.serializables.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonsRepository @Inject constructor(
    val dao: PokemonsDao,
    val api: PokemonsService
) {

    suspend fun insertPokemons(pokemons: List<Pokemon>) = dao.insertPokemons(pokemons)

    fun readPokemons(): Flow<List<Pokemon>> = dao.readPokemons()

    suspend fun deleteAllData() = dao.deleteAllData()

    suspend fun responseGetPokemons(limit: Int = 50, startPoint: Int): List<Result> {
        val resultsList = mutableListOf<Result>()
        val response = api.getPokemon(limit, startPoint)
        if (response.isSuccessful) {
            val results = response.body()!!.results
            for (every in results) {
                resultsList.add(Result(every.name, every.url))
            }
        }
        return resultsList
    }

    suspend fun responseGetPokemonsImage(startPoint: Int, endPoint: Int): List<String> {
        val imagesUrlList = mutableListOf<String>()
        for (number in startPoint..endPoint) {
            Log.d("POKEMON", number.toString())
            val url = "https://pokeapi.co/api/v2/pokemon-form/$number/"
            val response = api.getImageForPokemon(url)
            if (response.isSuccessful) {
                val imageUrl = response.body()!!.sprites.front_default
                imagesUrlList.add(imageUrl)
            }
        }
        return imagesUrlList
    }
}