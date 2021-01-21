package com.example.pokmons.feature.pokemons.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
import com.example.pokmons.util.RequestState
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
            if (wipeList() && isConnected() || viewmodel.pokemons.first().isEmpty() && isConnected()){
                viewmodel.deleteAllData()
                var offset = getOffset()
                if (offset > 1249) {
                    saveOffset(0)
                    offset = 0
                }
                viewmodel.responseGetPokemonsImage(offset)
                viewmodel.offsetChannel.send(offset + 50)
                saveOffset(offset + 50)
            } else if (!isConnected()) {
                usersActivity.snackbarMessage("There is a problem with your connection. Check your wifi and internet.")
                val offset = getOffset()
                viewmodel.offsetChannel.send(offset)
            }
            else {
                val offset = getOffset()
                viewmodel.offsetChannel.send(offset)
            }
        }

        setupAdapter()
        setupRefresh()

    }

    private suspend fun getOffset(): Int {
        val key = intPreferencesKey("offset")
        val offset = datastore.data.first()[key] ?: 0
        return offset
    }

    private suspend fun saveOffset(offset: Int) {
        val key = intPreferencesKey("offset")
        datastore.edit {
            it[key] = offset
        }
    }

    private fun setupRefresh() {
        lifecycleScope.launch {
            viewmodel.refresh.asFlow().collect {
                if (it && viewmodel.pokemons.first().isEmpty() && isConnected()) {
                    viewmodel.responseGetPokemonsImage(0)
                    viewmodel.offsetChannel.send(50)
                    viewmodel.refresh.send(false)
                } else if (it) {
                    usersActivity.snackbarMessage("Option only available if your list is empty and you have connection to WIFI!")
                    viewmodel.refresh.send(false)
                }
            }
        }

    }

    private fun isConnected(): Boolean {
        val connectivityManager = usersActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkInfo = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(networkInfo)!!
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo!!.isConnected
        }
    }

    private suspend fun wipeList(): Boolean {
        val key = longPreferencesKey("LastUpdate")
        val lastStamp: Long = datastore.data.first()[key] ?: 0
        return System.currentTimeMillis() > lastStamp
    }

    private fun setupAdapter() {
        lifecycleScope.launch {
            viewmodel.pokemons.collect {
                pokemonsAdapter.submitData(it)
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
            if (viewmodel.requestState.value == RequestState.EMPTY) {
                val startPoint = viewmodel.offsetChannel.value
                viewmodel.responseGetPokemonsImage(startPoint)
                viewmodel.offsetChannel.send(startPoint + 50)
                saveOffset(startPoint + 50)
            }
        }
    }

    private fun requestNewDataMinus() {
        isScrolling = false
        lifecycleScope.launch {
            if (viewmodel.requestState.value == RequestState.EMPTY) {
                val startPoint = viewmodel.offsetChannel.value
                viewmodel.responseGetPokemonsImage(startPoint - viewmodel.pokemons.first().size - 50)
            }
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
            val firstIndex = rv.findFirstVisibleItemPosition()

            if (totalItems-1 == lastIndex && totalItems > 0 && isScrolling && dy > 0) {
                if (!isConnected()) usersActivity.snackbarMessage("There is a problem with your connection. Check your wifi and internet.")
                else requestNewData()
            } else if (firstIndex == 0 && totalItems > 0 && isScrolling && dy < 0) {
                if (!isConnected()) usersActivity.snackbarMessage("There is a problem with your connection. Check your wifi and internet.")
                else requestNewDataMinus()
            }
            super.onScrolled(recyclerView, dx, dy)
        }
    }
}