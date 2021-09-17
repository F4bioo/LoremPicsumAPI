package com.fappslab.lorempicsumapi.di

import com.fappslab.lorempicsumapi.data.repository.LocalDataRepository
import com.fappslab.lorempicsumapi.data.repository.Repository
import com.fappslab.lorempicsumapi.data.room.PhotoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataRepositoryModule {

    @Singleton
    @Provides
    fun provideLocalDataRepository(
        dao: PhotoDao
    ): Repository.LocalData {
        return LocalDataRepository(dao)
    }
}
