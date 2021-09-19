package com.fappslab.lorempicsumapi.di

import android.content.Context
import android.content.SharedPreferences
import com.fappslab.lorempicsumapi.utils.Constants
import com.fappslab.lorempicsumapi.utils.Prefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(
            Constants.PREFERENCES, Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun providePrefs(
        prefs: SharedPreferences
    ): Prefs {
        return Prefs(prefs)
    }
}
