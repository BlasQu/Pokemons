package com.example.pokmons.feature.pokemons.logic

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokmons.data.entities.Pokemon
import com.example.pokmons.data.serializables.PokemonInfo
import com.example.pokmons.data.serializables.pokemon.name.Results
import com.example.pokmons.data.serializables.Stats
import com.example.pokmons.util.RequestState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class PokemonsViewModel @ViewModelInject constructor(
    val repository: PokemonsRepository
): ViewModel() {

    val offsetChannel = ConflatedBroadcastChannel<Int>(0)
    val pokemonInfo = ConflatedBroadcastChannel<PokemonInfo>()
    val pokemons: Flow<List<Pokemon>> = repository.readPokemons()
    val requestState = MutableStateFlow<RequestState>(RequestState.EMPTY)


    fun insertPokemons(pokemons: List<Pokemon>) {
        viewModelScope.launch {
            repository.insertPokemons(pokemons)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch {
            repository.deleteAllData()
        }
    }

    suspend fun responseGetPokemonsImage(startPoint: Int) {
        var pokemonsList = listOf<Results>()
        var pokemonsImagesList = listOf<String>()
        var pokemonsStats = listOf<Stats>()

        withContext(viewModelScope.coroutineContext) {
            requestState.value = RequestState.LOADING
            launch { pokemonsList = repository.responseGetPokemons(startPoint) }
            launch { pokemonsImagesList = repository.responseGetPokemonsImage(startPoint) }
            launch { pokemonsStats = repository.responseGetPokemonsAbilities(startPoint) }
        }

        if (pokemonsList.size == pokemonsImagesList.size && pokemonsList.size == pokemonsStats.size) {
            val innerPokemons = mutableListOf<Pokemon>()
            for (i in pokemonsList.indices) {
                val name = pokemonsList[i].name
                val imageUrl = pokemonsImagesList[i]
                val stats = listOf(pokemonsStats[i])
                innerPokemons.add(Pokemon(0, name, imageUrl, stats))
            }
            requestState.value = RequestState.SUCCESS
            insertPokemons(innerPokemons)
        } else {
            requestState.value = RequestState.ERROR("Error occurred while requesting data.")
        }
    }
}