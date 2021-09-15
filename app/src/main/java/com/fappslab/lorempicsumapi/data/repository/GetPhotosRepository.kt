package com.fappslab.lorempicsumapi.data.repository

import com.fappslab.lorempicsumapi.data.model.Photo
import retrofit2.Response

interface GetPhotosRepository {

    suspend fun getPhotos(page: Int, limit: Int): Response<List<Photo>?>?

}
