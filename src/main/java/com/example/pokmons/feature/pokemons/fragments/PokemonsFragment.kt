package com.example.pokmons.feature.pokemons.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokmons.R
import com.example.pokmons.databinding.FragmentPokemonsBinding
import com.example.pokmons.feature.pokemons.user.UsersActivity
import com.example.pokmons.feature.pokemons.adapters.PokemonsAdapter
import com.example.pokmons.feature.pokemons.logic.PokemonsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PokemonsFragment @Inject constructor(
): Fragment(R.layout.fragment_pokemons) {

    private val viewmodel by activityViewModels<PokemonsViewModel>()
    private lateinit var binding: FragmentPokemonsBinding
    private lateinit var usersActivity: UsersActivity

    private var isScrolling = false

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
                viewmodel.offsetChannel.send(50)
            } else {
                viewmodel.offsetChannel.send(viewmodel.pokemons.first().size)
            }
        }

        setupAdapter()

    }

    private suspend fun wipeList(): Boolean {
        val key = longPreferencesKey("LastUpdate")
        var lastStamp: Long = datastore.data.first()[key] ?: 0
        return System.currentTimeMillis() > lastStamp
    }

    private fun setupAdapter() {
        lifecycleScope.launch {
            viewmodel.pokemons.collect {
                pokemonsAdapter.submitData(it)
                viewmodel.offsetChannel.send(it.size)
            }
        }

        pokemonsAdapter.clickListener = usersActivity.setClickListenerOnAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(usersActivity)
            adapter = pokemonsAdapter
            addOnScrollListener(scrollListener)
        }
    }

    private fun requestNewData() {
        isScrolling = false
        lifecycleScope.launch {
            val startPoint = viewmodel.offsetChannel.value
            viewmodel.responseGetPokemonsImage(startPoint)
            viewmodel.offsetChannel.send(startPoint + 50)
        }
    }

    val scrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            isScrolling = true
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val rv = recyclerView.layoutManager as LinearLayoutManager

            val totalItems = rv.itemCount
            val lastIndex = rv.findLastVisibleItemPosition()

            if (totalItems-1 == lastIndex && totalItems > 0 && isScrolling) {
                requestNewData()
            }
            super.onScrolled(recyclerView, dx, dy)
        }
    }
}