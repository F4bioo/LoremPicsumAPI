package com.fappslab.lorempicsumapi.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fappslab.lorempicsumapi.data.model.PhotoEntity

@Database(entities = [PhotoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    companion object {
        var DATABASE_NAME: String = "photo.db"
    }
}
