package com.fappslab.lorempicsumapi.data.repository

import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import com.fappslab.lorempicsumapi.data.room.PhotoDao
import com.fappslab.lorempicsumapi.utils.extensions.fromPhotoToEntity
import javax.inject.Inject

class LocalRepository
@Inject
constructor(
    private val dao: PhotoDao
) : Repository.LocalData {
    override suspend fun setFavorite(photo: PhotoEntity): Long {
        return dao.setFavorite(photo = photo)
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
}
