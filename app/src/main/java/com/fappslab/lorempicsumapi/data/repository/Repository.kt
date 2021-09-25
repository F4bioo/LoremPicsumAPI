package com.fappslab.lorempicsumapi.data.repository

import com.fappslab.lorempicsumapi.data.model.PhotoDomain
import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import retrofit2.Response

interface Repository {

    interface RemoteData {
        suspend fun getPhotos(page: Int, limit: Int): Response<List<PhotoDomain>?>?
    }

    interface LocalData {
        suspend fun setFavorite(photo: PhotoEntity): Int

        suspend fun getFavorite(id: Long): PhotoEntity

        suspend fun getFavorites(): List<PhotoEntity>
    }
}
