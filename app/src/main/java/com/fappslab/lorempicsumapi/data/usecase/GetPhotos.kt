package com.fappslab.lorempicsumapi.data.usecase

import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.repository.GetPhotosRepository
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.data.state.parseResponse
import com.fappslab.lorempicsumapi.utils.BaseUseCase
import javax.inject.Inject

class GetPhotos
@Inject
constructor(
    private val repository: GetPhotosRepository
) : BaseUseCase<DataState<List<Photo>?>?, GetPhotos.Params> {

    override suspend fun invoke(params: Params): DataState<List<Photo>>? {
        return try {
            when (val response =
                repository.getPhotos(page = params.page, limit = params.limit).parseResponse()) {

                is DataState.OnSuccess -> response.data?.let { DataState.OnSuccess(it) }
                is DataState.OnError -> DataState.OnError(response.error)
                is DataState.OnException -> DataState.OnException(response.e)
            }
        } catch (e: Exception) {
            DataState.OnException(e)
        }
    }

    data class Params(
        val page: Int,
        val limit: Int
    )
}
