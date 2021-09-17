package com.fappslab.lorempicsumapi.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "_id")
    val _id: Long,
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
    @ColumnInfo(name = "downloadUrl")
    val downloadUrl: String,
    @ColumnInfo(name = "grayScale")
    var grayScale: Float = 1f,
    @ColumnInfo(name = "favorite")
    var favorite: Boolean = false
)
