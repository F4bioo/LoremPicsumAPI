package com.fappslab.lorempicsumapi.data.repository

import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import com.fappslab.lorempicsumapi.data.room.PhotoDao
import javax.inject.Inject

class LocalDataRepository
@Inject
constructor(
    private val dao: PhotoDao
) : Repository.LocalData {
    override suspend fun setFavorite(photo: Photo): Long {
        return dao.setFavorite(photo = photo.toEntity())
    }

    override suspend fun deleteFavorite(id: Long): Int {
        return dao.deleteFavorite(id = id)
    }

    override suspend fun getFavorite(id: Long): PhotoEntity {
        return dao.getFavorite(id = id)
    }

    override suspend fun getFavorites(): List<PhotoEntity> {
        return dao.getFavorites()
    }

    private fun Photo.toEntity(): PhotoEntity {
        return PhotoEntity(
            _id = this.id.toLong(),
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
