package com.fappslab.lorempicsumapi.data.usecase

import com.fappslab.lorempicsumapi.data.api.DataState
import com.fappslab.lorempicsumapi.data.api.parseResponse
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.repository.RemoteRepository
import com.fappslab.lorempicsumapi.utils.Constants
import com.fappslab.lorempicsumapi.utils.extensions.fromDomainsToPhotos
import javax.inject.Inject

class GetPhotos
@Inject
constructor(
    private val repository: RemoteRepository
) : BaseUseCase.Params<DataState<List<Photo>?>?, GetPhotos.Params> {

    override suspend fun invoke(params: Params): DataState<List<Photo>>? {
        return when (val response =
            repository.getPhotos(page = params.page, limit = params.limit).parseResponse()) {
            is DataState.OnSuccess -> response.data?.let {
                println("<> Repository")
                DataState.OnSuccess(it.fromDomainsToPhotos())
            }
            is DataState.OnException -> DataState.OnException(response.e)
            is DataState.OnError -> DataState.OnError(response.errorBody, response.code)
        }
    }

    data class Params(
        val page: Int,
        val limit: Int = Constants.PAGE_SIZE
    )
}
