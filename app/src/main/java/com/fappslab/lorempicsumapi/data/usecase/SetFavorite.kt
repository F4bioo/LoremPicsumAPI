package com.fappslab.lorempicsumapi.data.usecase

import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.repository.LocalDataRepository
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.utils.BaseUseCase
import javax.inject.Inject

class SetFavorite
@Inject
constructor(
    private val repository: LocalDataRepository
) : BaseUseCase.Params<DataState<Boolean>, SetFavorite.Params> {

    override suspend fun invoke(params: Params): DataState<Boolean> {
        return try {
            DataState.OnSuccess(repository.insert(params.photo) != -1L)
        } catch (e: Exception) {
            DataState.OnException(e)
        }
    }

    data class Params(
        val photo: Photo
    )
}
