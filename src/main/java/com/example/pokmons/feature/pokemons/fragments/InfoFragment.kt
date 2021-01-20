package com.example.pokmons.feature.pokemons.fragments

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokmons.R
import com.example.pokmons.databinding.FragmentInfoBinding
import com.example.pokmons.feature.pokemons.UsersActivity
import com.example.pokmons.feature.pokemons.fragments.adapters.InfoAbilityAdapter
import com.example.pokmons.feature.pokemons.fragments.adapters.InfoFormsAdapter
import com.example.pokmons.feature.pokemons.logic.PokemonsViewModel
import com.example.pokmons.util.Divider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class InfoFragment @Inject constructor(
    val abilityAdapter: InfoAbilityAdapter,
    val formsAdapter: InfoFormsAdapter
): Fragment(R.layout.fragment_info) {

    val viewmodel by activityViewModels<PokemonsViewModel>()
    private lateinit var binding: FragmentInfoBinding
    private lateinit var usersActivity: UsersActivity


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentInfoBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        usersActivity = activity as UsersActivity

        lifecycleScope.launch {
            initInfo()
        }
    }

    private suspend fun initInfo() {
        viewmodel.pokemonInfo.asFlow().collect {
            binding.textIdUser.text = it.id
            binding.textNameUser.text = it.name
            binding.textWeightUser.text = it.weight
            binding.textHeightUser.text = it.height

            val imageUri = Uri.parse(it.imageUrl)
            binding.imgPokemonSprite.setImageURI(imageUri, usersActivity)

            setupAbilityAdapter(it.abilities)
            setupFormsAdapter(it.types)
        }
    }

    private fun setupAbilityAdapter(newList: List<String>) {
        val divider = Divider(usersActivity)
        divider.setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.divider, usersActivity.theme)!!)
        binding.rvAbilities.apply {
            layoutManager = LinearLayoutManager(usersActivity)
            adapter = abilityAdapter
            addItemDecoration(divider)
        }

        abilityAdapter.submitData(newList)
    }

    private fun setupFormsAdapter(newList: List<String>) {
        val divider = Divider(usersActivity)
        divider.setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.divider, usersActivity.theme)!!)
        binding.rvForms.apply {
            layoutManager = LinearLayoutManager(usersActivity)
            adapter = formsAdapter
            addItemDecoration(divider)
        }

        formsAdapter.submitData(newList)
    }
}