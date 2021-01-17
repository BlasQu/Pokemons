package com.example.pokmons.feature.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import com.example.pokmons.R
import com.example.pokmons.databinding.ActivitySplashBinding
import com.example.pokmons.feature.pokemons.PokemonsActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animateSplash()
        changeActivity()

    }

    private fun changeActivity() {
        val intent = Intent(this, PokemonsActivity::class.java)
        lifecycleScope.launch {
            delay(3000)
            startActivity(intent)
            finish()
        }
    }

    private fun animateSplash() {
        val shortAnimTime = resources.getInteger(R.integer.short_anim_time).toLong()
        binding.imgPokemonLogo.animate().apply {
            alpha(1f)
            duration = shortAnimTime
            interpolator = FastOutSlowInInterpolator()
        }
    }
}