package com.example.pokmons.feature.pokemons.logic

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pokmons.data.entities.Pokemon
import com.example.pokmons.util.DiffCallback
import com.example.pokmons.databinding.RvPokemonItemBinding
import javax.inject.Inject

class PokemonsAdapter @Inject constructor(
): RecyclerView.Adapter<PokemonsAdapter.ViewHolder>() {

    var pokemonsList: MutableList<Pokemon> = mutableListOf()

    inner class ViewHolder(val binding: RvPokemonItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                // Proceed to Pokemon Details - Bulba, bulba, bulbasaur
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
        val result = DiffUtil.calculateDiff(DiffCallback(pokemonsList, newList))
        pokemonsList.apply {
            clear()
            addAll(newList)
        }
        result.dispatchUpdatesTo(this)
    }
}