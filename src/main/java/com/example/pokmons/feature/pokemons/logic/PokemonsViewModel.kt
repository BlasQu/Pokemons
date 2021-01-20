package com.example.pokmons.feature.pokemons.logic

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokmons.data.entities.Pokemon
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
    val pokemonInfo = ConflatedBroadcastChannel<Int>(0)
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
        var pokemonsList = listOf<String>()
        var pokemonsImagesList = listOf<String>()

        withContext(viewModelScope.coroutineContext) {
            requestState.value = RequestState.LOADING
            launch { pokemonsList = repository.responseGetPokemons(startPoint) }
            launch { pokemonsImagesList = repository.responseGetPokemonsImage(startPoint) }
        }

        Log.d("POKEMON", pokemonsList.size.toString())
        Log.d("POKEMON", pokemonsImagesList.size.toString())
        if (pokemonsList.size == pokemonsImagesList.size) {
            val innerPokemons = mutableListOf<Pokemon>()
            for (i in pokemonsList.indices) {
                val name = pokemonsList[i]
                val imageUrl = pokemonsImagesList[i]
                innerPokemons.add(Pokemon(0, name, imageUrl))
            }
            requestState.value = RequestState.SUCCESS
            insertPokemons(innerPokemons)
        } else {
            requestState.value = RequestState.ERROR("Error occurred while requesting data.")
        }
    }

    fun testAbilities() {
        viewModelScope.launch {
            repository.responseGetPokemonsAbilities()
        }
    }
}