package com.fappslab.lorempicsumapi.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import com.fappslab.lorempicsumapi.data.model.RemoteKeys

@Database(version = 1, entities = [PhotoEntity::class, RemoteKeys::class])
abstract class PhotosDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
    abstract fun getKeysDao(): RemoteKeyDao

}
