package com.fappslab.lorempicsumapi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.data.usecase.GetFavorite
import com.fappslab.lorempicsumapi.data.usecase.GetPhotos
import com.fappslab.lorempicsumapi.data.usecase.SetFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val getPhotos: GetPhotos,
    private val getFavorite: GetFavorite,
    private val setFavorite: SetFavorite
) : ViewModel() {
    private val _getPhotosEvent = MutableLiveData<DataState<List<Photo>>?>()
    val getPhotosEvent: LiveData<DataState<List<Photo>>?> get() = _getPhotosEvent

    private val _insertEvent = MutableLiveData<DataState<Boolean>>()
    val insertEvent: LiveData<DataState<Boolean>> get() = _insertEvent

    fun getPhotos(page: Int = 1) {
        val list = mutableListOf<Photo>()

        viewModelScope.launch {
            val photos = getPhotos.invoke(GetPhotos.Params(page = page))
            if (photos is DataState.OnSuccess) {
                photos.data.forEach {
                    it.favorite = getFavorite(it)
                    list.add(it)
                }
                _getPhotosEvent.value = DataState.OnSuccess(list)
            } else _getPhotosEvent.value = photos
        }
    }

    fun setFavorite(photo: Photo) {
        viewModelScope.launch {
            _insertEvent.value = setFavorite.invoke(SetFavorite.Params(photo = photo))
        }
    }

    fun checkFavorites(list: List<Photo>): List<Photo> {
        val photos = mutableListOf<Photo>()
        viewModelScope.launch {
            list.forEach {
                it.favorite = getFavorite(it)
                photos.add(it)
            }
        }
        return photos
    }

    private suspend fun getFavorite(photo: Photo): Boolean {
        return when (getFavorite.invoke(GetFavorite.Params(photo.id.toLong()))) {
            is DataState.OnSuccess -> true
            else -> false
        }
    }
}
