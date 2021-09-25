package com.fappslab.lorempicsumapi.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fappslab.lorempicsumapi.data.model.RemoteKeyEntity

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(remoteKeys: List<RemoteKeyEntity>)

    @Query("SELECT * FROM remote_keys WHERE photoId = :id")
    suspend fun remoteKeysPhotoId(id: String): RemoteKeyEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAll()
}
