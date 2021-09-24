package com.fappslab.lorempicsumapi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.fappslab.lorempicsumapi.data.api.DataState
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.usecase.GetMediatorData
import com.fappslab.lorempicsumapi.data.usecase.GetPagingData
import com.fappslab.lorempicsumapi.data.usecase.GetPhotos
import com.fappslab.lorempicsumapi.data.usecase.SetFavorite
import com.fappslab.lorempicsumapi.utils.extensions.fromEntityToPhoto
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
    private val getPagingData: GetPagingData,
    private val getMediatorData: GetMediatorData
) : ViewModel() {
    private val _getPhotosEvent = MutableLiveData<DataState<List<Photo>>?>()
    val getPhotosEvent: LiveData<DataState<List<Photo>>?>
        get() = _getPhotosEvent

    private val _pagingEvent = MutableLiveData<PagingData<Photo>>()
    val pagingEvent: LiveData<PagingData<Photo>>
        get() = _pagingEvent

    private val _insertEvent = MutableLiveData<DataState<Boolean>>()
    val insertEvent: LiveData<DataState<Boolean>>
        get() = _insertEvent

    init {
        getPhotosFromMediator()
    }

    fun setFavorite(photo: Photo) {
        viewModelScope.launch {
            setFavorite.invoke(SetFavorite.Params(photo = photo))
        }
    }

    fun getPhotosFromPagingSource() {
        viewModelScope.launch {
            getPagingData.invoke().cachedIn(viewModelScope).collect {
                _pagingEvent.value = it
            }
        }
    }

    private fun getPhotosFromMediator() {
        viewModelScope.launch {
            getMediatorData.invoke().cachedIn(viewModelScope).collect {
                _pagingEvent.value = it.map { entity ->
                    entity.fromEntityToPhoto()
                }
            }
        }
    }
}
