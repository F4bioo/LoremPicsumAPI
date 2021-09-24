package com.fappslab.lorempicsumapi.data.usecase

import com.fappslab.lorempicsumapi.data.api.DataState
import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import com.fappslab.lorempicsumapi.data.repository.LocalRepository
import javax.inject.Inject

class GetAllPhotos
@Inject
constructor(
    private val repository: LocalRepository
) : BaseUseCase.Empty<DataState<List<PhotoEntity>>> {

    override suspend fun invoke(): DataState<List<PhotoEntity>> {
        return try {
            DataState.OnSuccess(repository.getAllPhotos())
        } catch (e: Exception) {
            DataState.OnException(e)
        }
    }
}
