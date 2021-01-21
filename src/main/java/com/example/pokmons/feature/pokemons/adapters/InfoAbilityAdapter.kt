package com.example.pokmons.feature.pokemons.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokmons.databinding.RvAbilityItemBinding
import java.util.*
import javax.inject.Inject

class InfoAbilityAdapter @Inject constructor(
): RecyclerView.Adapter<InfoAbilityAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RvAbilityItemBinding): RecyclerView.ViewHolder(binding.root)

    private val abilityList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvAbilityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textAbility.text = abilityList[position].capitalize(Locale.ROOT)
    }

    override fun getItemCount(): Int {
        return abilityList.size
    }

    fun submitData(newList: List<String>) {
        abilityList.clear()
        abilityList.addAll(newList)
        notifyDataSetChanged()
    }
}