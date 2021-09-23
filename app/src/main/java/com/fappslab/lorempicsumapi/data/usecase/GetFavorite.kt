package com.fappslab.lorempicsumapi.data.usecase

import com.fappslab.lorempicsumapi.data.api.DataState
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.repository.LocalRepository
import com.fappslab.lorempicsumapi.utils.extensions.fromEntityToPhoto
import javax.inject.Inject

class GetFavorite
@Inject
constructor(
    private val repository: LocalRepository
) : BaseUseCase.Params<DataState<Photo>, GetFavorite.Params> {

    override suspend fun invoke(params: Params): DataState<Photo> {
        return try {
            DataState.OnSuccess(repository.getFavorite(params.id).fromEntityToPhoto())
        } catch (e: Exception) {
            DataState.OnException(e)
        }
    }

    data class Params(
        val id: Long
    )
}
