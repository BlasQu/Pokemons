package com.example.pokmons.feature.splash

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import com.example.pokmons.R
import com.example.pokmons.databinding.ActivitySplashBinding
import com.example.pokmons.feature.pokemons.UsersActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animateSplash()
        changeActivity()
        setupStats()

    }

    private fun changeActivity() {
        val intent = Intent(this, UsersActivity::class.java)
        lifecycleScope.launch {
            delay(3000)
            startActivity(intent)
            finish()
        }
    }

    private fun setupStats() {
        val usageStatsManager: UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE)!! as UsageStatsManager
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR, -1)
        val queryStatsManager: List<UsageStats> = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, calendar.timeInMillis, System.currentTimeMillis())
        for (every in queryStatsManager) {
            if (every.packageName == packageName) {
                val date = SimpleDateFormat("HH:mm:ss").format(every.lastTimeVisible)
                Log.d("NOTPOKEMON", date.toString())
            }
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