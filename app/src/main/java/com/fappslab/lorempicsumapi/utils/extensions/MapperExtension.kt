package com.fappslab.lorempicsumapi.utils.extensions

import com.fappslab.lorempicsumapi.data.model.FavoriteEntity
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.model.PhotoDomain
import com.fappslab.lorempicsumapi.data.model.PhotoEntity

fun PhotoDomain.fromPhotoDomainToPhoto(): Photo {
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

fun PhotoEntity.fromPhotoEntityToPhoto(): Photo {
    return Photo(
        id = this.id,
        author = this.author,
        width = this.width,
        height = this.height,
        url = this.url,
        downloadUrl = this.downloadUrl
    )
}

fun FavoriteEntity.fromPhotoEntityToPhoto(): Photo {
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

fun Photo.fromPhotoToPhotoEntity(): PhotoEntity {
    return PhotoEntity(
        id = this.id,
        author = this.author,
        width = this.width,
        height = this.height,
        url = this.url,
        downloadUrl = this.downloadUrl
    )
}

fun Photo.fromPhotoToFavoriteEntity(): FavoriteEntity {
    return FavoriteEntity(
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
    return this.map { it.fromPhotoDomainToPhoto() }
}

fun List<FavoriteEntity>.fromEntitiesToPhotos(): List<Photo> {
    return this.map { it.fromPhotoEntityToPhoto() }
}

fun List<Photo>.fromDomainsToEntities(): List<PhotoEntity> {
    return this.map { it.fromPhotoToPhotoEntity() }
}
