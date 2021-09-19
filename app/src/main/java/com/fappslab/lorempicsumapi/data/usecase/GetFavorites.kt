package com.fappslab.lorempicsumapi.data.usecase

import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import com.fappslab.lorempicsumapi.data.repository.LocalDataRepository
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.utils.BaseUseCase
import javax.inject.Inject

class GetFavorites
@Inject
constructor(
    private val repository: LocalDataRepository
) : BaseUseCase.Empty<DataState<List<Photo>>> {

    override suspend fun invoke(): DataState<List<Photo>> {
        return try {
            DataState.OnSuccess(repository.getFavorites().toPhotos())
        } catch (e: Exception) {
            DataState.OnException(e)
        }
    }

    private fun List<PhotoEntity>.toPhotos(): List<Photo> {
        val photos = arrayListOf<Photo>()
        for (obj in this) {
            val photo = Photo(
                id = obj.id,
                author = obj.author,
                width = obj.width,
                height = obj.height,
                url = obj.url,
                downloadUrl = obj.downloadUrl,
                favorite = obj.favorite
            )
            photos.add(photo)
        }
        return photos
    }
}
