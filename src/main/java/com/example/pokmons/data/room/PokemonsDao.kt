package com.example.pokmons.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pokmons.data.entities.Pokemon
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemons(pokemons: List<Pokemon>)

    @Query("SELECT * FROM pokemons")
    fun readPokemons(): Flow<List<Pokemon>>

    @Query("DELETE FROM pokemons")
    suspend fun deleteAllData()

}