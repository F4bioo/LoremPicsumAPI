package com.fappslab.lorempicsumapi.di

import android.content.Context
import com.fappslab.lorempicsumapi.utils.CheckNetwork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideCheckNetworkConnection(
        @ApplicationContext context: Context
    ): CheckNetwork {
        return CheckNetwork(context)
    }
}
