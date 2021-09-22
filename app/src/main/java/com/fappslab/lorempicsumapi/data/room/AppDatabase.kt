package com.fappslab.lorempicsumapi.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import com.fappslab.lorempicsumapi.data.model.RemoteKey

@Database(version = 1, entities = [PhotoEntity::class, RemoteKey::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao // TODO RENAME TO getPhotoDao
    abstract fun getKeysDao(): RemoteKeyDao

    companion object {
        var DATABASE_NAME: String = "photo.db"
    }
}
