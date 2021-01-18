package com.example.pokmons.feature.pokemons

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokmons.R
import com.example.pokmons.data.api.PokemonsService
import com.example.pokmons.data.entities.Pokemon
import com.example.pokmons.databinding.ActivityPokemonsBinding
import com.example.pokmons.databinding.ProgressBarBinding
import com.example.pokmons.util.RequestState
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
    lateinit var pokemonsAdapter: PokemonsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewmodel.deleteAllData()
        setupAdapter()
        setupCollectors()

        lifecycleScope.launch {
            viewmodel.responseGetPokemonsImage(1, 50)
        }
        /*
        lifecycleScope.launch {
            val response = api.getPokemon()
            if (response.isSuccessful) {

                val results = response.body()!!.results
                val pokemonsList = mutableListOf<Pokemon>()

                for (every in results) {

                    val id = Regex("[0-9]+").findAll(every.url).map(MatchResult::value).toList()
                    val url = "https://pokeapi.co/api/v2/pokemon-form/${id[1]}/"

                    lifecycleScope.launch {

                        val imageResponse = api.getImageForPokemon(url)
                        if (imageResponse.isSuccessful) {
                            pokemonsList.add(Pokemon(0, every.name, every.url, imageResponse.body()!!.sprites.front_default))
                            Log.d("POKEMON", pokemonsList.toString())
                        }
                    }
                }
            }
        }

         */
    }

    private fun setupCollectors() {
        lifecycleScope.launch {
            viewmodel.pokemons.collect {
                pokemonsAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            val shortAnimTime = resources.getInteger(R.integer.short_anim_time).toLong()
            viewmodel.requestState.collect {
                when (it) {
                    RequestState.SUCCESS -> {
                        binding.holderProgressBar.holderProgressBar.animate().apply {
                            duration = shortAnimTime
                            interpolator = FastOutSlowInInterpolator()
                            alpha(0f)
                        }
                        delay(shortAnimTime)
                        binding.holderProgressBar.holderProgressBar.visibility = View.VISIBLE
                        viewmodel.requestState.value = RequestState.EMPTY

                    }
                    RequestState.LOADING -> {
                        binding.holderProgressBar.holderProgressBar.animate().apply {
                            binding.holderProgressBar.holderProgressBar.visibility = View.VISIBLE
                            duration = shortAnimTime
                            interpolator = FastOutSlowInInterpolator()
                            alpha(0.75f)
                        }
                    }
                }
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