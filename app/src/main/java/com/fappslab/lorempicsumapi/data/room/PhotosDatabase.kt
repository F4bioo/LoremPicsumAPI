package com.fappslab.lorempicsumapi.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fappslab.lorempicsumapi.data.model.PhotoEntity

@Database(entities = [PhotoEntity::class], version = 1)
abstract class PhotosDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}
