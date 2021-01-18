package com.example.pokmons.feature.pokemons

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokmons.data.api.PokemonsService
import com.example.pokmons.data.entities.Pokemon
import com.example.pokmons.databinding.ActivityPokemonsBinding
import com.example.pokmons.feature.pokemons.adapters.PokemonsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PokemonsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonsBinding
    private val viewmodel by viewModels<PokemonsViewModel>()

    @Inject
    lateinit var api: PokemonsService

    @Inject
    lateinit var pokemonsAdapter: PokemonsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewmodel.deleteAllData()
        setupAdapter()

        lifecycleScope.launch {
            viewmodel.pokemons.collect {
                pokemonsAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            val response = api.getPokemon()
            if (response.isSuccessful) {
                val results = response.body()!!.results
                val pokemonsList = mutableListOf<Pokemon>()
                for (every in results) {
                    pokemonsList.add(Pokemon(0, every.name, every.url))
                }
                viewmodel.insertPokemons(pokemonsList)
            }
        }
    }

    private fun setupAdapter() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PokemonsActivity)
            adapter = pokemonsAdapter
        }
    }
}