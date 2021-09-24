package com.fappslab.lorempicsumapi.data.repository

import androidx.paging.PagingSource
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import com.fappslab.lorempicsumapi.data.room.PhotoDao
import com.fappslab.lorempicsumapi.data.room.PhotosDatabase
import com.fappslab.lorempicsumapi.utils.extensions.fromPhotoToEntity
import javax.inject.Inject

class LocalRepository
@Inject
constructor(
    private val db: PhotosDatabase
) : Repository.LocalData {

    override suspend fun setFavorite(photo: PhotoEntity): Long {
        return db.photoDao().setFavorite(photo = photo)
    }

    override suspend fun deleteFavorite(id: Long): Int {
        return db.photoDao().deleteFavorite(id = id)
    }

    override suspend fun getFavorite(id: Long): PhotoEntity {
        return db.photoDao().getFavorite(id = id)
    }

    override suspend fun getFavorites(): List<PhotoEntity> {
        return db.photoDao().getFavorites()
    }

    override suspend fun getAllPhotos(): List<PhotoEntity> {
        return db.photoDao().getAllPhotos()
    }
}
