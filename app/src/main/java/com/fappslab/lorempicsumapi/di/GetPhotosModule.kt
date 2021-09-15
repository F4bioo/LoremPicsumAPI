package com.fappslab.lorempicsumapi.di

import com.fappslab.lorempicsumapi.data.repository.GetPhotosImplRepository
import com.fappslab.lorempicsumapi.data.repository.GetPhotosRepository
import com.fappslab.lorempicsumapi.data.service.Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GetPhotosModule {

    @Singleton
    @Provides
    fun provideGetPhotosImplRepository(
        service: Service
    ): GetPhotosRepository {
        return GetPhotosImplRepository(service)
    }
}
