package com.fappslab.lorempicsumapi.di

import android.content.Context
import androidx.room.Room
import com.fappslab.lorempicsumapi.data.room.FavoriteDao
import com.fappslab.lorempicsumapi.data.room.PhotoDao
import com.fappslab.lorempicsumapi.data.room.PhotosDatabase
import com.fappslab.lorempicsumapi.data.room.RemoteKeyDao
import com.fappslab.lorempicsumapi.utils.Constants
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
    ): PhotosDatabase {
        return Room.databaseBuilder(
            context, PhotosDatabase::class.java,
            Constants.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providePhotoDao(
        db: PhotosDatabase
    ): PhotoDao {
        return db.photoDao()
    }

    @Singleton
    @Provides
    fun provideRemoteKeyDao(
        db: PhotosDatabase
    ): RemoteKeyDao {
        return db.keyDao()
    }

    @Singleton
    @Provides
    fun provideFavoriteDao(
        db: PhotosDatabase
    ): FavoriteDao {
        return db.favoriteDao()
    }
}
