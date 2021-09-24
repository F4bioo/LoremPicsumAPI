package com.fappslab.lorempicsumapi.di

import com.fappslab.lorempicsumapi.data.api.ApiService
import com.fappslab.lorempicsumapi.data.repository.LocalRepository
import com.fappslab.lorempicsumapi.data.repository.RemoteRepository
import com.fappslab.lorempicsumapi.data.repository.Repository
import com.fappslab.lorempicsumapi.data.room.PhotoDao
import com.fappslab.lorempicsumapi.data.room.PhotosDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRemoteRepository(
        api: ApiService
    ): Repository.RemoteData {
        return RemoteRepository(api)
    }

    @Singleton
    @Provides
    fun provideLocalRepository(
        db: PhotosDatabase
    ): Repository.LocalData {
        return LocalRepository(db)
    }
}
