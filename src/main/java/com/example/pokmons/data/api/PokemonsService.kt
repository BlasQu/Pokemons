package com.example.pokmons.data.api

import com.example.pokmons.data.entities.PokemonsList
import retrofit2.Response
import retrofit2.http.GET

interface PokemonsService {

    @GET("pokemon?limit=50&offset=0")
    suspend fun getPokemon(): Response<PokemonsList>
}