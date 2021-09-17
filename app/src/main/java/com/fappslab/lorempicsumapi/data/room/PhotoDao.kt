package com.fappslab.lorempicsumapi.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fappslab.lorempicsumapi.data.model.PhotoEntity

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: PhotoEntity): Long

    @Query("SELECT * FROM photo WHERE id = :id")
    suspend fun photo(id: Long): PhotoEntity

    @Query("SELECT * FROM photo")
    suspend fun photos(): List<PhotoEntity>
}
