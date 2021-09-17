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
    override suspend fun insert(photo: Photo): Long {
        return dao.insert(photo = photo.toEntity())
    }

    override suspend fun photo(id: Long): PhotoEntity {
        return dao.photo(id = id)
    }

    override suspend fun photos(): List<PhotoEntity> {
        return dao.photos()
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
