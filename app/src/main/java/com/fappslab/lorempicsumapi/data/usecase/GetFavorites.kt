package com.fappslab.lorempicsumapi.data.usecase

import com.fappslab.lorempicsumapi.data.api.DataState
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.repository.LocalRepository
import com.fappslab.lorempicsumapi.utils.extensions.fromEntitiesToPhotos
import javax.inject.Inject

class GetFavorites
@Inject
constructor(
    private val repository: LocalRepository
) : BaseUseCase.Empty<DataState<List<Photo>>> {

    override suspend fun invoke(): DataState<List<Photo>> {
        return try {
            DataState.OnSuccess(repository.getFavorites().fromEntitiesToPhotos())
        } catch (e: Exception) {
            DataState.OnException(e)
        }
    }
}
