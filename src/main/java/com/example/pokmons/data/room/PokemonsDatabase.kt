package com.example.pokmons.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokmons.data.converters.JSONConverter
import com.example.pokmons.data.entities.Pokemon

@Database(entities = [Pokemon::class], version = 1, exportSchema = false)
@TypeConverters(JSONConverter::class)
abstract class PokemonsDatabase : RoomDatabase() {
    abstract fun getDao() : PokemonsDao

}