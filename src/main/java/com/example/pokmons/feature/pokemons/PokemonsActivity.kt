package com.example.pokmons.feature.pokemons

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.pokmons.data.entities.Pokemon
import com.example.pokmons.databinding.ActivityPokemonsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonsBinding
    private val viewmodel by viewModels<PokemonsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}