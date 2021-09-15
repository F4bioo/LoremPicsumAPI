package com.fappslab.lorempicsumapi.data.service

import com.fappslab.lorempicsumapi.data.model.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("/v2/list")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<List<Photo>?>?
}
