package com.fappslab.lorempicsumapi.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.data.usecase.GetPhotos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val getPhotos: GetPhotos
) : ViewModel() {
    private val _getPhotosEvent = MutableLiveData<DataState<List<Photo>>>()
    val getPhotosEvent: LiveData<DataState<List<Photo>>> get() = _getPhotosEvent

    fun getPhotos(page: Int = 1, limit: Int = 10) {
        viewModelScope.launch {
            _getPhotosEvent.value = getPhotos.invoke(GetPhotos.Params(page = page, limit = limit))
        }
    }
}
