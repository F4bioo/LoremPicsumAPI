package com.fappslab.lorempicsumapi.data.room

import androidx.paging.PagingSource
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

    @Query("SELECT * FROM photo WHERE favorite = 1 ORDER BY id ASC")
    suspend fun getFavorites(): List<PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(photos: List<PhotoEntity>)

    @Query("SELECT * FROM photo ORDER BY id ASC")
    fun getAll(): PagingSource<Int, PhotoEntity>

    @Query("SELECT * FROM photo ORDER BY id ASC")
    suspend fun getAllPhotos(): List<PhotoEntity>

    @Query("DELETE FROM photo WHERE favorite = 0")
    suspend fun deleteAll()
}
