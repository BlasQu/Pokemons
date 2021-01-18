package com.example.pokmons.feature.pokemons

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokmons.data.entities.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PokemonsViewModel @ViewModelInject constructor(
    val repository: PokemonsRepository
): ViewModel() {

    val pokemons: Flow<List<Pokemon>> = repository.readPokemons()


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
}