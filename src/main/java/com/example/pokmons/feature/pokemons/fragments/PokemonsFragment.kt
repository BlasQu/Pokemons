package com.example.pokmons.feature.pokemons.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.size
import androidx.fragment.app.activityViewModels
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PokemonsFragment @Inject constructor(
): Fragment(R.layout.fragment_pokemons) {

    private val viewmodel by activityViewModels<PokemonsViewModel>()
    private lateinit var binding: FragmentPokemonsBinding
    private lateinit var usersActivity: UsersActivity
    var startPoint = 50

    @Inject
    lateinit var pokemonsAdapter: PokemonsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentPokemonsBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        usersActivity = activity as UsersActivity

        setupAdapter()
    }

    private fun setupAdapter() {
        lifecycleScope.launch {
            viewmodel.pokemons.collect {
                pokemonsAdapter.submitData(it)
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
                            startPoint += 50
                        }
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }
    }
}