package com.fappslab.lorempicsumapi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.data.usecase.GetFavorite
import com.fappslab.lorempicsumapi.data.usecase.GetPhotos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val getPhotos: GetPhotos,
    private val getFavorite: GetFavorite
) : ViewModel() {
    private val _getPhotosEvent = MutableLiveData<DataState<List<Photo>>?>()
    val getPhotosEvent: LiveData<DataState<List<Photo>>?> get() = _getPhotosEvent

    fun getPhotos(page: Int = 1, limit: Int = 10) {
        val list = mutableListOf<Photo>()
        viewModelScope.launch {
            val photos = getPhotos.invoke(GetPhotos.Params(page = page, limit = limit))
            if (photos is DataState.OnSuccess) {
                photos.data.forEach { list.add(checkDatabase(it)) }
                _getPhotosEvent.value = DataState.OnSuccess(list)
            } else _getPhotosEvent.value = photos
        }
    }

    private suspend fun checkDatabase(photo: Photo): Photo {
        return when (val it = getFavorite.invoke(GetFavorite.Params(photo.id.toLong()))) {
            is DataState.OnSuccess -> it.data
            else -> photo
        }
    }
}
