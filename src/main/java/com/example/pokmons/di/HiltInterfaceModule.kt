package com.example.pokmons.di

import com.example.pokmons.feature.pokemons.UsersActivity
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@FlowPreview
@ExperimentalCoroutinesApi
@Module
@InstallIn(ApplicationComponent::class)
abstract class HiltInterfaceModule {

    @Binds
    abstract fun bindsClickListener(
            clickListenerImpl: UsersActivity.ClickListenerImpl
    ): UsersActivity.ClickListener
}