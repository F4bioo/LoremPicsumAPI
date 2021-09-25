package com.fappslab.lorempicsumapi.data.repository

import com.fappslab.lorempicsumapi.data.model.FavoriteEntity
import com.fappslab.lorempicsumapi.data.model.PhotoDomain
import retrofit2.Response

interface Repository {

    interface RemoteData {
        suspend fun getPhotos(page: Int, limit: Int): Response<List<PhotoDomain>?>?
    }

    interface LocalData {
        suspend fun setFavorite(favorite: FavoriteEntity): Long

        suspend fun getFavorite(id: String): FavoriteEntity?

        suspend fun getFavorites(): List<FavoriteEntity>?

        suspend fun delFavorite(favorite: FavoriteEntity): Int
    }
}
