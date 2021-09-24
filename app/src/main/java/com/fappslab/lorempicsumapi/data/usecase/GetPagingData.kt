package com.fappslab.lorempicsumapi.data.usecase

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.ui.adapter.paging.PhotoPagingSource
import com.fappslab.lorempicsumapi.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
class GetPagingData
@Inject
constructor(
    private val getPhotos: GetPhotos
) : BaseUseCase.Empty<Flow<PagingData<Photo>>> {

    override suspend fun invoke(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PhotoPagingSource(getPhotos) }
        ).flow
    }
}
