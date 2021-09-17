package com.fappslab.lorempicsumapi.di

import com.fappslab.lorempicsumapi.data.repository.RemoteDataRepository
import com.fappslab.lorempicsumapi.data.repository.Repository
import com.fappslab.lorempicsumapi.data.api.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataRepositoryModule {

    @Singleton
    @Provides
    fun provideRemoteDataRepository(
        api: Api
    ): Repository.RemoteData {
        return RemoteDataRepository(api)
    }
}
