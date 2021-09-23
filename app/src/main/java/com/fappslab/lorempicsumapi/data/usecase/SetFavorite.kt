package com.fappslab.lorempicsumapi.data.usecase

import com.fappslab.lorempicsumapi.data.api.DataState
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.repository.LocalRepository
import com.fappslab.lorempicsumapi.utils.extensions.fromPhotoToEntity
import javax.inject.Inject

class SetFavorite
@Inject
constructor(
    private val repository: LocalRepository
) : BaseUseCase.Params<DataState<Boolean>, SetFavorite.Params> {

    override suspend fun invoke(params: Params): DataState<Boolean> {
        return try {
            DataState.OnSuccess(repository.setFavorite(params.photo.fromPhotoToEntity()) != -1L)
        } catch (e: Exception) {
            DataState.OnException(e)
        }
    }

    data class Params(
        val photo: Photo
    )
}
