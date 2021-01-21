package com.example.pokmons.data.api

import com.example.pokmons.data.serializables.pokemon.image.PokemonImage
import com.example.pokmons.data.serializables.pokemon.stats.PokemonStats
import com.example.pokmons.data.serializables.pokemon.name.PokemonsList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonsService {

    @GET("pokemon")
    suspend fun getPokemon(
            @Query("limit") limit: Int = 50,
            @Query("offset") offset: Int = 0
    ): Response<PokemonsList>

    @GET
    suspend fun getAbilities(@Url url: String): Response<PokemonStats>

    @GET
    suspend fun getImageUrl(@Url url: String): Response<PokemonImage>
}