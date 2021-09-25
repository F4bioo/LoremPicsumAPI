package com.fappslab.lorempicsumapi.data.repository

import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import com.fappslab.lorempicsumapi.data.room.PhotosDatabase
import javax.inject.Inject

class LocalRepository
@Inject
constructor(
    private val db: PhotosDatabase
) : Repository.LocalData {

    override suspend fun setFavorite(photo: PhotoEntity): Int {
        return db.photoDao().setFavorite(photo = photo)
    }

    override suspend fun getFavorite(id: Long): PhotoEntity {
        return db.photoDao().getFavorite(id = id)
    }

    override suspend fun getFavorites(): List<PhotoEntity> {
        return db.photoDao().getFavorites()
    }
}
