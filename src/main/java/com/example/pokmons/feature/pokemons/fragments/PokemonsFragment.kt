package com.example.pokmons.feature.pokemons.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.size
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokmons.R
import com.example.pokmons.databinding.FragmentPokemonsBinding
import com.example.pokmons.feature.pokemons.logic.PokemonsAdapter
import com.example.pokmons.feature.pokemons.UsersActivity
import com.example.pokmons.feature.pokemons.logic.PokemonsViewModel
import com.example.pokmons.util.Divider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@AndroidEntryPoint
class PokemonsFragment @Inject constructor(
): Fragment(R.layout.fragment_pokemons) {

    private val viewmodel by activityViewModels<PokemonsViewModel>()
    private lateinit var binding: FragmentPokemonsBinding
    private lateinit var usersActivity: UsersActivity
    var startPoint: Int = 50

    @Inject
    lateinit var pokemonsAdapter: PokemonsAdapter

    @Inject
    lateinit var datastore: DataStore<Preferences>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentPokemonsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        usersActivity = activity as UsersActivity

        lifecycleScope.launch {
            if (wipeList() || viewmodel.pokemons.first().isEmpty()){
                viewmodel.deleteAllData()
                viewmodel.responseGetPokemonsImage(0)
            }
        }

        setupAdapter()

    }

    private suspend fun wipeList(): Boolean {
        val key = longPreferencesKey("LastUpdate")
        var lastStamp: Long = datastore.data.first()[key] ?: 0
        Log.d("NOTPOKEMON", lastStamp.toString())
        return System.currentTimeMillis() > lastStamp
    }

    private suspend fun writeToDatastore() {
        val key = longPreferencesKey("LastUpdate")
        datastore.edit {
            it[key] = System.currentTimeMillis() + 600000 // 10 minutes
        }
    }

    private fun setupAdapter() {
        lifecycleScope.launch {
            viewmodel.pokemons.collect {
                pokemonsAdapter.submitData(it)
                startPoint = it.size
            }
        }

        val divider = Divider(usersActivity)
        divider.setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.divider, usersActivity.theme)!!)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(usersActivity)
            adapter = pokemonsAdapter
            addItemDecoration(divider)
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val rv = recyclerView.layoutManager as LinearLayoutManager

                    val totalItems = rv.itemCount
                    val lastIndex = rv.findLastVisibleItemPosition()

                    if (totalItems-1 == lastIndex && totalItems > 0) {
                        lifecycleScope.launch {
                            viewmodel.responseGetPokemonsImage(startPoint)
                            writeToDatastore()
                            startPoint += 50
                        }
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }
    }
}