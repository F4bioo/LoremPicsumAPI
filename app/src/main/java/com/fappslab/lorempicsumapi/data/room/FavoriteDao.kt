package com.fappslab.lorempicsumapi.data.room

import androidx.room.*
import com.fappslab.lorempicsumapi.data.model.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setFavorite(favoriteEntity: FavoriteEntity): Long

    @Query("SELECT * FROM favorites WHERE id = :id")
    suspend fun getFavorite(id: String): FavoriteEntity?

    @Query("SELECT * FROM favorites")
    suspend fun getFavorites(): List<FavoriteEntity>?

    @Delete
    suspend fun delFavorite(favoriteEntity: FavoriteEntity): Int
}
