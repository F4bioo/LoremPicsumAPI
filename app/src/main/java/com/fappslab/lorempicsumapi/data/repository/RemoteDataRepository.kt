package com.fappslab.lorempicsumapi.data.repository

import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.api.ApiService
import retrofit2.Response
import javax.inject.Inject

class RemoteDataRepository
@Inject
constructor(
    private val api: ApiService
) : Repository.RemoteData {
    override suspend fun getPhotos(page: Int, limit: Int): Response<List<Photo>?>? {
        return api.getPhotos(page = page, limit = limit)
    }
}
