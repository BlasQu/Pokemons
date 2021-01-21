package com.example.pokmons.feature.pokemons.user

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import com.example.pokmons.R
import com.example.pokmons.data.serializables.PokemonInfo
import com.example.pokmons.databinding.ActivityUsersBinding
import com.example.pokmons.feature.pokemons.fragments.InfoFragment
import com.example.pokmons.feature.pokemons.fragments.PokemonsFragment
import com.example.pokmons.feature.pokemons.logic.PokemonsViewModel
import com.example.pokmons.util.RequestState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class UsersActivity: AppCompatActivity() {

    private lateinit var binding: ActivityUsersBinding
    private val viewmodel by viewModels<PokemonsViewModel>()

    @Inject
    lateinit var pokemonsFragment: PokemonsFragment

    @Inject
    lateinit var infoFragment: InfoFragment

    @Inject
    lateinit var datastore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCollectors()
        setupFragment()
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar.root)
        val toolbar = supportActionBar!!
        toolbar.apply {
            title = "Pokemons"
            setDisplayHomeAsUpEnabled(false)
        }
    }
    
    private fun currentFragment(): String {
        val entry = supportFragmentManager.backStackEntryCount - 1
        val fragment = supportFragmentManager.getBackStackEntryAt(entry).name!!
        return fragment
    }

    override fun onBackPressed() {
        val fragment = currentFragment()
        when (fragment) {
            "PokemonsFragment" -> {
                finish()
            }
            "InfoFragment" -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, pokemonsFragment)
                    addToBackStack("PokemonsFragment")
                    commit()
                }
                supportActionBar!!.apply {
                    title = "Pokemons"
                    setDisplayHomeAsUpEnabled(false)
                }
            }
        }
    }

    private fun setupFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, pokemonsFragment)
            addToBackStack("PokemonsFragment")
            commit()
        }
    }

    private fun changeFragment() {
        val fragment = currentFragment()
        when (fragment) {
            "PokemonsFragment" -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, infoFragment)
                    addToBackStack("InfoFragment")
                    commit()
                }
            }
            "InfoFragment" -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, pokemonsFragment)
                    addToBackStack("PokemonsFragment")
                    commit()
                }
            }
        }
    }

    fun snackbarMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private suspend fun writeToDatastore() {
        val key = longPreferencesKey("LastUpdate")
        datastore.edit {
            it[key] = System.currentTimeMillis() + 600000 // 10 minutes
        }
    }

    private fun setupCollectors() {
        lifecycleScope.launch {
            val shortAnimTime = resources.getInteger(R.integer.short_anim_time).toLong()
            viewmodel.requestState.collect {
                when (it) {
                    RequestState.SUCCESS -> {
                        binding.holderProgressBar.holderProgressBar.animate().apply {
                            duration = shortAnimTime
                            interpolator = FastOutSlowInInterpolator()
                            alpha(0f)
                        }
                        delay(shortAnimTime)
                        viewmodel.requestState.value = RequestState.EMPTY
                        writeToDatastore()
                    }
                    RequestState.LOADING -> {
                        binding.holderProgressBar.holderProgressBar.visibility = View.VISIBLE
                        binding.holderProgressBar.holderProgressBar.animate().apply {
                            duration = shortAnimTime
                            interpolator = FastOutSlowInInterpolator()
                            alpha(0.75f)
                        }
                    }
                    is RequestState.ERROR -> {
                        binding.holderProgressBar.holderProgressBar.animate().apply {
                            duration = shortAnimTime
                            interpolator = FastOutSlowInInterpolator()
                            alpha(0f)
                        }
                        delay(shortAnimTime)
                        viewmodel.requestState.value = RequestState.EMPTY
                        snackbarMessage(it.message)
                    }
                    RequestState.EMPTY -> binding.holderProgressBar.holderProgressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setClickListenerOnAdapter(): ClickListener {
        return object: ClickListener {
            override fun click(pokemonInfo: PokemonInfo) {
                lifecycleScope.launch {
                    viewmodel.pokemonInfo.send(pokemonInfo)
                }
                changeFragment()
                supportActionBar!!.apply {
                    title = "Pokemon details"
                    setDisplayHomeAsUpEnabled(true)
                }
            }
        }
    }

    interface ClickListener {
        fun click(pokemonInfo: PokemonInfo)
    }


    class ClickListenerImpl @Inject constructor(
    ): ClickListener {
        override fun click(pokemonInfo: PokemonInfo) {
        }
    }

}