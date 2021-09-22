package com.fappslab.lorempicsumapi.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fappslab.lorempicsumapi.data.model.RemoteKey

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE photoId = :id")
    suspend fun remoteKeysPhotoId(id: String): RemoteKey?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAll()
}
