package com.fappslab.lorempicsumapi.data.repository

import com.fappslab.lorempicsumapi.data.model.FavoriteEntity
import com.fappslab.lorempicsumapi.data.room.FavoriteDao
import javax.inject.Inject

class LocalRepository
@Inject
constructor(
    private val dao: FavoriteDao,
) : Repository.LocalData {

    override suspend fun setFavorite(favorite: FavoriteEntity): Long {
        return dao.setFavorite(favorite)
    }

    override suspend fun getFavorite(id: String): FavoriteEntity? {
        return dao.getFavorite(id)
    }

    override suspend fun getFavorites(): List<FavoriteEntity>? {
        return dao.getFavorites()
    }

    override suspend fun delFavorite(favorite: FavoriteEntity): Int {
        return dao.delFavorite(favorite)
    }
}
