package com.fappslab.lorempicsumapi.data.usecase

import com.fappslab.lorempicsumapi.data.api.DataState
import com.fappslab.lorempicsumapi.data.repository.LocalRepository
import javax.inject.Inject

class DeleteFavorite
@Inject
constructor(
    private val repository: LocalRepository
) : BaseUseCase.Params<DataState<Boolean>, DeleteFavorite.Params> {

    override suspend fun invoke(params: Params): DataState<Boolean> {
        return try {
            DataState.OnSuccess(repository.deleteFavorite(params.id) != -1)
        } catch (e: Exception) {
            DataState.OnException(e)
        }
    }

    data class Params(
        val id: Long
    )
}
