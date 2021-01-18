package com.example.pokmons.feature.pokemons

import com.example.pokmons.data.serializables.Result
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokmons.data.entities.Pokemon
import com.example.pokmons.util.RequestState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PokemonsViewModel @ViewModelInject constructor(
    val repository: PokemonsRepository
): ViewModel() {

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

    suspend fun responseGetPokemonsImage(startPoint: Int, endPoint: Int) {
        var pokemonsList = listOf<Result>()
        var pokemonsImagesList = listOf<String>()

        val response = viewModelScope.launch {
            requestState.value = RequestState.LOADING
            pokemonsList = repository.responseGetPokemons(50, startPoint)
            pokemonsImagesList = repository.responseGetPokemonsImage(startPoint, endPoint)
        }.join()

        if (pokemonsList.size == pokemonsImagesList.size) {
            val innerPokemons = mutableListOf<Pokemon>()
            for (i in 0..49) {
                val name = pokemonsList[i].name
                val url = pokemonsList[i].url
                val imageUrl = pokemonsImagesList[i]
                innerPokemons.add(Pokemon(0, name, url, imageUrl))
            }
            requestState.value = RequestState.SUCCESS
            insertPokemons(innerPokemons)
        }
    }
}