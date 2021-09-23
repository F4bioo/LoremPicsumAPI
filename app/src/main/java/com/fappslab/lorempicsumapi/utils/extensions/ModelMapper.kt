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

fun List<PhotoDomain>.fromDomainToPhotos(): List<Photo> {
    val photos = arrayListOf<Photo>()
    for (photo in this) {
        photos.add(photo.fromDomainToPhoto())
    }
    return photos
}

fun List<PhotoEntity>.fromEntityToPhotos(): List<Photo> {
    val photos = arrayListOf<Photo>()
    for (photo in this) {
        photos.add(photo.fromEntityToPhoto())
    }
    return photos
}
