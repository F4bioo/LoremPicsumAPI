package com.fappslab.lorempicsumapi.data.usecase

import com.fappslab.lorempicsumapi.data.repository.LocalDataRepository
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.utils.BaseUseCase
import javax.inject.Inject

class DeleteFavorite
@Inject
constructor(
    private val repository: LocalDataRepository
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
