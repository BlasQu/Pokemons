package com.example.pokmons.feature.pokemons.fragments.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pokmons.data.entities.Pokemon
import com.example.pokmons.databinding.RvPokemonItemBinding
import com.example.pokmons.data.serializables.PokemonInfo
import com.example.pokmons.feature.pokemons.UsersActivity
import com.example.pokmons.util.PokemonDiffCallback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class PokemonsAdapter @Inject constructor(
        var clickListener: UsersActivity.ClickListener
): RecyclerView.Adapter<PokemonsAdapter.ViewHolder>() {

    var pokemonsList: MutableList<Pokemon> = mutableListOf()


    inner class ViewHolder(val binding: RvPokemonItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val id = pokemonsList[adapterPosition].id.toString()
                val name = pokemonsList[adapterPosition].name
                val abilities = pokemonsList[adapterPosition].stats.first().abilities
                val weight = pokemonsList[adapterPosition].stats.first().weight.toString()
                val height = pokemonsList[adapterPosition].stats.first().height.toString()
                val types = pokemonsList[adapterPosition].stats.first().types
                val imageURL = pokemonsList[adapterPosition].imageUrl

                val pokemonInfo = PokemonInfo(id, name, abilities, types, weight, height, imageURL)
                clickListener.click(pokemonInfo)
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
