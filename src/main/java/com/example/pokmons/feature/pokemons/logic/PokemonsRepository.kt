package com.example.pokmons.feature.pokemons.logic

import com.example.pokmons.data.api.PokemonsService
import com.example.pokmons.data.entities.Pokemon
import com.example.pokmons.data.room.PokemonsDao
import com.example.pokmons.data.serializables.PokemonConnector
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

    suspend fun responseGetPokemons(startPoint: Int): List<PokemonConnector> {
        val pokemons = mutableListOf<PokemonConnector>()
        val response = api.getPokemon(50, startPoint)
        var pokemonWeight = 0
        var pokemonHeight = 0

        if (response.isSuccessful) {
            val results = response.body()!!.results
            for (every in results) {
                val pokemonName = every.name
                val pokemonId = getPokemonId(every.url)
                var imageUrl = ""
                val url = "https://pokeapi.co/api/v2/pokemon/${pokemonId}/"
                val response2 = api.getAbilities(url)
                if (response2.isSuccessful) {
                    val abilities = mutableListOf<String>()
                    val types = mutableListOf<String>()
                    val body = response2.body()!!
                    pokemonWeight = body.weight
                    pokemonHeight = body.height
                    val formUrl = body.forms.first().url
                    if (pokemonId < 900) imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemonId}.png"
                    // ID: 900+ can't exploit easier way of implementing images
                    else imageUrl = respondGetPokemonImage(formUrl)

                    for (e in body.abilities) {
                        abilities.add(e.ability.name)
                    }
                    for (e in body.types) {
                        types.add(e.type.name)
                    }

                    val stats = Stats(abilities, pokemonWeight, pokemonHeight, types)
                    pokemons.add(PokemonConnector(pokemonId, pokemonName, imageUrl, stats))
                }
            }
        }
        return pokemons
    }

    suspend fun respondGetPokemonImage(formUrl: String): String {
        val response = api.getImageUrl(formUrl)
        if (response.isSuccessful) {
            val imageBody = response.body()!!
            val imageUrl: String? = imageBody.sprites.front_default
            return imageUrl ?: "error"
        }
        return "error"
    }

        fun getPokemonId(url: String): Int {
            val id: List<String> = Regex("[0-9]+").findAll(url).map(MatchResult::value).toList()
            return id.last().toInt()
        }
    }