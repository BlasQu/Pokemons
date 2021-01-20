package com.example.pokmons.feature.pokemons.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.pokmons.R
import com.example.pokmons.databinding.FragmentInfoBinding
import com.example.pokmons.feature.pokemons.UsersActivity
import com.example.pokmons.feature.pokemons.logic.PokemonsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class InfoFragment @Inject constructor(
): Fragment(R.layout.fragment_info) {

    val viewmodel by activityViewModels<PokemonsViewModel>()
    private lateinit var binding: FragmentInfoBinding
    private lateinit var usersActivity: UsersActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentInfoBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        usersActivity = activity as UsersActivity

        initInfo()
    }

    private fun initInfo() {
        binding.textHeight.text = "Djasdaj"
    }
}