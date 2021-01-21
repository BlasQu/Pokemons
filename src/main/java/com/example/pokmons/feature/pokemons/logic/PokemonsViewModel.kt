package com.example.pokmons.feature.pokemons.logic

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokmons.data.entities.Pokemon
import com.example.pokmons.data.serializables.PokemonConnector
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
    val refresh = ConflatedBroadcastChannel<Boolean>()


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
        var pokemonsList = listOf<PokemonConnector>()
        var pokemonsStats = listOf<Stats>()

        withContext(viewModelScope.coroutineContext) {
            requestState.value = RequestState.LOADING
            pokemonsList = repository.responseGetPokemons(startPoint)
        }

        if (pokemonsList.isNotEmpty()) {
            val innerPokemons = mutableListOf<Pokemon>()
            for (i in pokemonsList.indices) {
                val name = pokemonsList[i].pokemonName
                val pokemonId = pokemonsList[i].pokemonId
                val imageUrl = pokemonsList[i].imagesUrl
                val stats = listOf(pokemonsList[i].stats)
                innerPokemons.add(Pokemon(0, pokemonId, name, imageUrl, stats))
            }
            requestState.value = RequestState.SUCCESS
            insertPokemons(innerPokemons)
        } else {
            requestState.value = RequestState.ERROR("Error occurred while requesting data.")
        }
    }
}