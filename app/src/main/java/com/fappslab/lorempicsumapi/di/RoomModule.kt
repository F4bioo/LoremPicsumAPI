package com.fappslab.lorempicsumapi.di

import android.content.Context
import androidx.room.Room
import com.fappslab.lorempicsumapi.data.room.AppDatabase
import com.fappslab.lorempicsumapi.data.room.PhotoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providePhotoDao(
        appDatabase: AppDatabase
    ): PhotoDao {
        return appDatabase.photoDao()
    }
}
