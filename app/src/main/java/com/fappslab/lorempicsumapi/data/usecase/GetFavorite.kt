package com.fappslab.lorempicsumapi.data.usecase

import com.fappslab.lorempicsumapi.data.api.DataState
import com.fappslab.lorempicsumapi.data.repository.LocalRepository
import javax.inject.Inject

class GetFavorite
@Inject
constructor(
    private val repository: LocalRepository
) : BaseUseCase.Params<DataState<Boolean>, GetFavorite.Params> {

    override suspend fun invoke(params: Params): DataState<Boolean> {
        return try {
            DataState.OnSuccess(repository.getFavorite(params.id) != null)
        } catch (e: Exception) {
            DataState.OnException(e)
        }
    }

    data class Params(
        val id: String
    )
}
