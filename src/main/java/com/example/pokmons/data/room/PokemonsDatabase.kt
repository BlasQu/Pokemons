package com.example.pokmons.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokmons.data.entities.Pokemon

@Database(entities = [Pokemon::class], version = 1, exportSchema = false)
abstract class PokemonsDatabase : RoomDatabase() {
    abstract fun getDao() : PokemonsDao
}