package com.example.pokmons.feature.pokemons

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokmons.R
import com.example.pokmons.databinding.ActivityUsersBinding
import com.example.pokmons.feature.pokemons.fragments.PokemonsFragment
import com.example.pokmons.feature.pokemons.logic.PokemonsViewModel
import com.example.pokmons.util.Divider
import com.example.pokmons.util.RequestState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersBinding
    private val viewmodel by viewModels<PokemonsViewModel>()

    @Inject
    lateinit var pokemonsFragment: PokemonsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewmodel.deleteAllData()
        setupCollectors()
        setupFragment()

        lifecycleScope.launch {
            viewmodel.responseGetPokemonsImage(0)
        }
    }

    private fun setupFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, pokemonsFragment)
            addToBackStack("PokemonsFragment")
            commit()
        }
    }

    fun snackbarMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
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
                        delay(3000)
                        viewmodel.requestState.value = RequestState.EMPTY
                    }
                    RequestState.LOADING -> {
                        binding.holderProgressBar.holderProgressBar.animate().apply {
                            binding.holderProgressBar.holderProgressBar.visibility = View.VISIBLE
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
}