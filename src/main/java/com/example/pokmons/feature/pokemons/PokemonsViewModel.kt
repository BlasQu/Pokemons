package com.example.pokmons.feature.pokemons

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel

class PokemonsViewModel @ViewModelInject constructor(
    val repository: PokemonsRepository
): ViewModel() {
}