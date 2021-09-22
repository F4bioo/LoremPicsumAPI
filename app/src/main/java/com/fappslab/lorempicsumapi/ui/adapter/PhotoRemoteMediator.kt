package com.fappslab.lorempicsumapi.ui.adapter

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import com.fappslab.lorempicsumapi.data.model.RemoteKey
import com.fappslab.lorempicsumapi.data.room.AppDatabase
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.data.usecase.GetPhotos
import com.fappslab.lorempicsumapi.utils.Constants
import retrofit2.HttpException
import java.io.IOException

const val PAGE_INDEX = 1

@ExperimentalPagingApi
class PhotoRemoteMediator(
    private val api: GetPhotos,
    private val db: AppDatabase
) : RemoteMediator<Int, Photo>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Photo>
    ): MediatorResult {
        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        try {
            val photos = arrayListOf<Photo>()
            when (val response = api.invoke(GetPhotos.Params(page = page))) {
                is DataState.OnSuccess -> photos.addAll(response.data)
            }

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.photoDao().deleteAll()
                    db.getKeysDao().deleteAll()
                }
                val prevKey = if (page == PAGE_INDEX) null else page - 1
                val nextKey = if (photos.isEmpty()) null else page + 1
                val keys = photos.map {
                    RemoteKey(it.id, prevKey = prevKey, nextKey = nextKey)
                }
                db.getKeysDao().insertAll(keys)
                db.photoDao().insertAll(photos.toPhotoEntity())
            }
            return MediatorResult.Success(endOfPaginationReached = photos.isEmpty())

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Photo>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Photo>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.getKeysDao().remoteKeysPhotoId(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Photo>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { photo -> db.getKeysDao().remoteKeysPhotoId(photo.id) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Photo>): RemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { photo -> db.getKeysDao().remoteKeysPhotoId(photo.id) }
    }

    private fun Photo.toPhotoEntity(): PhotoEntity {
        return PhotoEntity(
            _id = this.id.toLong(),
            id = this.id,
            author = this.author,
            width = this.width,
            height = this.height,
            url = this.url,
            downloadUrl = this.downloadUrl,
            favorite = this.favorite
        )
    }

    private fun List<Photo>.toPhotoEntity(): List<PhotoEntity> {
        val photoEntity = arrayListOf<PhotoEntity>()
        for (photo in this) {
            photoEntity.add(photo.toPhotoEntity())
        }
        return photoEntity
    }
}
