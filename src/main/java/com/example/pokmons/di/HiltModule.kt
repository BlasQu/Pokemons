package com.example.pokmons.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pokmons.data.room.PokemonsDao
import com.example.pokmons.data.room.PokemonsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class HiltModule {

    @Singleton
    @Provides
    fun providesDatabase(
            @ApplicationContext context: Context
    ): PokemonsDatabase {
        return Room.databaseBuilder(context, PokemonsDatabase::class.java, "database_pokemons").build()
    }

    @Singleton
    @Provides
    fun providesDao(
            pokemonsDatabase: PokemonsDatabase): PokemonsDao {
        return pokemonsDatabase.getDao()
    }
}