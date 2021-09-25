package com.fappslab.lorempicsumapi.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import com.fappslab.lorempicsumapi.data.model.RemoteKeyEntity

@Database(entities = [PhotoEntity::class, RemoteKeyEntity::class], version = 1)
abstract class PhotosDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao

    abstract fun getKeysDao(): RemoteKeyDao
}
