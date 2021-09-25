package com.fappslab.lorempicsumapi.data.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fappslab.lorempicsumapi.data.model.PhotoEntity
import com.fappslab.lorempicsumapi.data.room.PhotosDatabase
import com.fappslab.lorempicsumapi.ui.adapter.paging.PhotoRemoteMediator
import com.fappslab.lorempicsumapi.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class GetMediatorData
@Inject
constructor(
    private val getPhotos: GetPhotos,
    private val db: PhotosDatabase
) : BaseUseCase.Empty<Flow<PagingData<PhotoEntity>>> {

    override suspend fun invoke(): Flow<PagingData<PhotoEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false,
            ),
            remoteMediator = PhotoRemoteMediator(
                getPhotos = getPhotos,
                db = db
            ),
            pagingSourceFactory = { db.photoDao().getAllPhotos() }
        ).flow
    }
}
