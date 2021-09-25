package com.fappslab.lorempicsumapi.data.room

import androidx.paging.PagingSource
import androidx.room.*
import com.fappslab.lorempicsumapi.data.model.PhotoEntity

@Dao
interface PhotoDao {
    @Update
    suspend fun setFavorite(photo: PhotoEntity): Int

    @Query("SELECT * FROM photos WHERE id = :id")
    suspend fun getFavorite(id: Long): PhotoEntity

    @Query("SELECT * FROM photos WHERE favorite = 1")
    suspend fun getFavorites(): List<PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<PhotoEntity>)

    @Query("SELECT * FROM photos ORDER BY id ASC")
    fun getAll(): PagingSource<Int, PhotoEntity>

    @Query("DELETE FROM photos WHERE favorite = 0")
    suspend fun deleteAll()
}
