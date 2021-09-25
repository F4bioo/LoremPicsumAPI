package com.fappslab.lorempicsumapi.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "width")
    val width: String,
    @ColumnInfo(name = "height")
    val height: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "download_url")
    val downloadUrl: String,
    @ColumnInfo(name = "favorite")
    var favorite: Boolean = false
)
