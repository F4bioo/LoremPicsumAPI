package com.fappslab.lorempicsumapi.data.repository

import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import retrofit2.Response

interface Repository {

    interface RemoteData {
        suspend fun getPhotos(page: Int, limit: Int): Response<List<Photo>?>?
    }

    interface LocalData {
        suspend fun insert(photo: Photo): Long

        suspend fun photo(id: Long): PhotoEntity

        suspend fun photos(): List<PhotoEntity>
    }
}
