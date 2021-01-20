package com.example.pokmons.data.api

import com.example.pokmons.data.serializables.PokemonStats
import com.example.pokmons.data.serializables.PokemonsList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonsService {

    @GET("pokemon")
    suspend fun getPokemon(
            @Query("limit") limit: Int = 50,
            @Query("offset") offset: Int = 0
    ): Response<PokemonsList>

    @GET("pokemon/77/")
    suspend fun getAbilities(): Response<PokemonStats>
}