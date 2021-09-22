package com.fappslab.lorempicsumapi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fappslab.lorempicsumapi.data.api.ApiService
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.room.AppDatabase
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.data.usecase.GetPhotos
import com.fappslab.lorempicsumapi.data.usecase.SetFavorite
import com.fappslab.lorempicsumapi.ui.adapter.PhotoRemoteMediator
import com.fappslab.lorempicsumapi.ui.adapter.RemotePagingSource
import com.fappslab.lorempicsumapi.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val getPhotos: GetPhotos,
    private val setFavorite: SetFavorite,
    private val database: AppDatabase,
    private val photoApi: ApiService
) : ViewModel() {
    private val _getPhotosEvent = MutableLiveData<DataState<List<Photo>>?>()
    val getPhotosEvent: LiveData<DataState<List<Photo>>?> get() = _getPhotosEvent

    private val _pagingEvent = MutableLiveData<PagingData<Photo>>()
    val pagingEvent: LiveData<PagingData<Photo>> get() = _pagingEvent

    private val _insertEvent = MutableLiveData<DataState<Boolean>>()
    val insertEvent: LiveData<DataState<Boolean>> get() = _insertEvent

    init {
        getPhotosFromMediator()
    }

    fun setFavorite(photo: Photo) {
        viewModelScope.launch {
            _insertEvent.value = setFavorite.invoke(SetFavorite.Params(photo = photo))
        }
    }

    private fun getPhotos() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = Constants.PAGE_SIZE,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { RemotePagingSource(getPhotos) }
            ).flow.cachedIn(viewModelScope).collect {
                _pagingEvent.value = it
            }
        }
    }

    @ExperimentalPagingApi
    fun getPhotosFromMediator() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = Constants.PAGE_SIZE,
                    enablePlaceholders = true,
                ),
                remoteMediator = PhotoRemoteMediator(
                    getPhotos,
                    database
                ),
                pagingSourceFactory = { database.photoDao().getAll() }
            ).flow.cachedIn(viewModelScope).collect {
                _pagingEvent.value = it
            }
        }
    }
}
