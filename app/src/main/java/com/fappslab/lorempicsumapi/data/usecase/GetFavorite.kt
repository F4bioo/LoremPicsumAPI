package com.fappslab.lorempicsumapi.data.usecase

import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import com.fappslab.lorempicsumapi.data.repository.LocalDataRepository
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.utils.BaseUseCase
import javax.inject.Inject

class GetFavorite
@Inject
constructor(
    private val repository: LocalDataRepository
) : BaseUseCase.Params<DataState<Photo>, GetFavorite.Params> {

    override suspend fun invoke(params: Params): DataState<Photo> {
        return try {
            DataState.OnSuccess(repository.getFavorite(params.id).toPhoto())
        } catch (e: Exception) {
            DataState.OnException(e)
        }
    }

    data class Params(
        val id: Long
    )

    private fun PhotoEntity.toPhoto(): Photo {
        return Photo(
            _id = this._id,
            id = this.id,
            author = this.author,
            width = this.width,
            height = this.height,
            url = this.url,
            downloadUrl = this.downloadUrl,
            favorite = this.favorite
        )
    }
}
