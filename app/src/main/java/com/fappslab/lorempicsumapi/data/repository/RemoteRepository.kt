package com.fappslab.lorempicsumapi.data.repository

import com.fappslab.lorempicsumapi.data.api.ApiService
import com.fappslab.lorempicsumapi.data.model.PhotoDomain
import retrofit2.Response
import javax.inject.Inject

class RemoteRepository
@Inject
constructor(
    private val api: ApiService
) : Repository.RemoteData {

    override suspend fun getPhotos(page: Int, limit: Int): Response<List<PhotoDomain>?>? {
        return api.getPhotos(page = page, limit = limit)
    }
}
