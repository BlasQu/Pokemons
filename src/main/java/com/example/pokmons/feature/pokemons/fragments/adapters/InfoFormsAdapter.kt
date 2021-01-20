package com.example.pokmons.feature.pokemons.fragments.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokmons.databinding.RvFormsItemBinding
import java.util.*
import javax.inject.Inject

class InfoFormsAdapter @Inject constructor(
): RecyclerView.Adapter<InfoFormsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RvFormsItemBinding): RecyclerView.ViewHolder(binding.root)

    private val formsList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvFormsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textForm.text = formsList[position].capitalize(Locale.ROOT)
    }

    override fun getItemCount(): Int {
        return formsList.size
    }

    fun submitData(newList: List<String>) {
        formsList.clear()
        formsList.addAll(newList)
        notifyDataSetChanged()
    }
}