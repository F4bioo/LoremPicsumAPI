package com.fappslab.lorempicsumapi.data.repository

import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.service.Service
import retrofit2.Response
import javax.inject.Inject

class GetPhotosImplRepository
@Inject
constructor(
    private val service: Service
) : GetPhotosRepository {
    override suspend fun getPhotos(page: Int, limit: Int): Response<List<Photo>?>? {
        return service.getPhotos(page = page, limit = limit)
    }
}
