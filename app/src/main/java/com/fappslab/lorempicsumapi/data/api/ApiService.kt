package com.fappslab.lorempicsumapi.data.api

import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/v2/list")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("limit") limit: Int = Constants.PAGE_SIZE
    ): Response<List<Photo>?>?
}
