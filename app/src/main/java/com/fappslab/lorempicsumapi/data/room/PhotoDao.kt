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
    suspend fun setAllPhotos(photos: List<PhotoEntity>)

    @Query("SELECT * FROM photos")
    fun getAllPhotos(): PagingSource<Int, PhotoEntity>

    @Query("DELETE FROM photos")
    suspend fun delAllPhotos()
}
