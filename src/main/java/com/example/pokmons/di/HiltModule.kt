package com.example.pokmons.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.room.Room
import com.example.pokmons.data.api.PokemonsService
import com.example.pokmons.data.consts.CONSTS
import com.example.pokmons.data.room.PokemonsDao
import com.example.pokmons.data.room.PokemonsDatabase
import com.google.gson.Gson
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class HiltModule {

    @Provides
    fun providesBaseUrl(): String = CONSTS.BASE_URL

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

    @Provides
    @Singleton
    fun providesDataStore(
            @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.createDataStore("timer")
    }

    @ExperimentalSerializationApi
    @Singleton
    @Provides
    fun providesRetrofit(
        BASE_URL: String
    ): Retrofit {
        val content = MediaType.get("application/json")
        val json = Json { ignoreUnknownKeys = true }.asConverterFactory(content)
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(json)
            .build()
    }

    @Singleton
    @Provides
    fun providesPokemonsService(
        retrofit: Retrofit
    ): PokemonsService {
        return retrofit.create(PokemonsService::class.java)
    }
}