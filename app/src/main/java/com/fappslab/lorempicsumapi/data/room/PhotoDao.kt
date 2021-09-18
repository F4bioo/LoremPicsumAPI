package com.fappslab.lorempicsumapi.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fappslab.lorempicsumapi.data.model.PhotoEntity

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setFavorite(photo: PhotoEntity): Long

    @Query("DELETE FROM photo WHERE id = :id")
    suspend fun deleteFavorite(id: Long): Int

    @Query("SELECT * FROM photo WHERE id = :id")
    suspend fun getFavorite(id: Long): PhotoEntity

    @Query("SELECT * FROM photo WHERE favorite = 1")
    suspend fun getFavorites(): List<PhotoEntity>
}
