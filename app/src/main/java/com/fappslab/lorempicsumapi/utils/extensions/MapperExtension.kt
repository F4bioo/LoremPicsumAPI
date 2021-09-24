package com.fappslab.lorempicsumapi.utils.extensions

import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.model.PhotoDomain
import com.fappslab.lorempicsumapi.data.model.PhotoEntity

fun PhotoDomain.fromDomainToPhoto(): Photo {
    return Photo(
        id = this.id,
        author = this.author,
        width = this.width,
        height = this.height,
        url = this.url,
        downloadUrl = this.downloadUrl,
        favorite = false
    )
}

fun PhotoEntity.fromEntityToPhoto(): Photo {
    return Photo(
        id = this.id,
        author = this.author,
        width = this.width,
        height = this.height,
        url = this.url,
        downloadUrl = this.downloadUrl,
        favorite = this.favorite
    )
}

fun Photo.fromPhotoToEntity(): PhotoEntity {
    return PhotoEntity(
        id = this.id,
        author = this.author,
        width = this.width,
        height = this.height,
        url = this.url,
        downloadUrl = this.downloadUrl,
        favorite = this.favorite
    )
}

fun List<PhotoDomain>.fromDomainsToPhotos(): List<Photo> {
    return this.map { it.fromDomainToPhoto() }
}

fun List<PhotoEntity>.fromEntitiesToPhotos(): List<Photo> {
    return this.map { it.fromEntityToPhoto() }
}

fun List<Photo>.fromDomainsToEntities(): List<PhotoEntity> {
    return this.map { it.fromPhotoToEntity() }
}
