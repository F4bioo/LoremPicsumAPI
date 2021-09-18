package com.fappslab.lorempicsumapi.data.repository

import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import retrofit2.Response

interface Repository {

    interface RemoteData {
        suspend fun getPhotos(page: Int): Response<List<Photo>?>?
    }

    interface LocalData {
        suspend fun setFavorite(photo: Photo): Long

        suspend fun deleteFavorite(id: Long): Int

        suspend fun getFavorite(id: Long): PhotoEntity

        suspend fun getFavorites(): List<PhotoEntity>
    }
}
