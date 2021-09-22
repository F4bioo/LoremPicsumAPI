package com.fappslab.lorempicsumapi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val photoId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
