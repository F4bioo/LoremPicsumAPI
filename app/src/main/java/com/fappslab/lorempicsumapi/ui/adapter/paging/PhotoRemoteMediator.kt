package com.fappslab.lorempicsumapi.ui.adapter.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.fappslab.lorempicsumapi.data.api.DataState
import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import com.fappslab.lorempicsumapi.data.model.RemoteKeyEntity
import com.fappslab.lorempicsumapi.data.room.PhotosDatabase
import com.fappslab.lorempicsumapi.data.usecase.GetPhotos
import com.fappslab.lorempicsumapi.utils.Constants
import com.fappslab.lorempicsumapi.utils.extensions.fromDomainsToEntities
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class PhotoRemoteMediator(
    private val getPhotos: GetPhotos,
    private val db: PhotosDatabase
) : RemoteMediator<Int, PhotoEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> return pageKeyData
            else -> pageKeyData as Int
        }

        try {
            val response = getPhotos.invoke(GetPhotos.Params(page))
            val photos = if (response is DataState.OnSuccess) response.data else arrayListOf()
            val isEndOfList = photos.isEmpty()

            db.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    db.photoDao().delAllPhotos()
                    db.keyDao().delAllRemoteKeys()
                }

                val prevKey = if (page == Constants.PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = photos.map {
                    RemoteKeyEntity(it.id, prevKey = prevKey, nextKey = nextKey)
                }

                db.photoDao().setAllPhotos(photos.fromDomainsToEntities())
                db.keyDao().setAllRemoteKeys(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKey(state)
                remoteKeys?.nextKey?.minus(1) ?: Constants.PAGE_INDEX
            }

            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }

            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                return remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
            }
        }
    }

    // get the closest remote key inserted which had the data
    private suspend fun getClosestRemoteKey(state: PagingState<Int, PhotoEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.keyDao().getRemoteKey(repoId)
            }
        }
    }

    // get the last remote key inserted which had the data
    private suspend fun getLastRemoteKey(state: PagingState<Int, PhotoEntity>): RemoteKeyEntity? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { photo -> db.keyDao().getRemoteKey(photo.id) }
    }

    // get the first remote key inserted which had the data
    private suspend fun getFirstRemoteKey(state: PagingState<Int, PhotoEntity>): RemoteKeyEntity? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { photo -> db.keyDao().getRemoteKey(photo.id) }
    }
}
