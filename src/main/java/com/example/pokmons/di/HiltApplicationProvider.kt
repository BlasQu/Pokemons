package com.example.pokmons.di

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltApplicationProvider : Application() {
    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    }
}