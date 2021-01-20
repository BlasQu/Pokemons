package com.example.pokmons.feature.pokemons.logic

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pokmons.data.entities.Pokemon
import com.example.pokmons.databinding.RvPokemonItemBinding
import com.example.pokmons.feature.pokemons.UsersActivity
import com.example.pokmons.feature.pokemons.fragments.PokemonsFragment
import com.example.pokmons.util.PokemonDiffCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class PokemonsAdapter @Inject constructor(
): RecyclerView.Adapter<PokemonsAdapter.ViewHolder>() {

    var pokemonsList: MutableList<Pokemon> = mutableListOf()

    inner class ViewHolder(val binding: RvPokemonItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvPokemonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUri: Uri = Uri.parse(pokemonsList[position].imageUrl)
        holder.binding.textPokemonId.text = pokemonsList[position].id.toString()
        holder.binding.textPokemonName.text = pokemonsList[position].name
        holder.binding.imgPokemon.setImageURI(imageUri, holder.binding.root.context)
    }

    override fun getItemCount(): Int {
        return pokemonsList.size
    }

    fun submitData(newList: List<Pokemon>) {
        val result = DiffUtil.calculateDiff(PokemonDiffCallback(pokemonsList, newList))
        pokemonsList.apply {
            clear()
            addAll(newList)
        }
        result.dispatchUpdatesTo(this)
    }
}