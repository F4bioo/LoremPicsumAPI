package com.fappslab.lorempicsumapi.ui.modal

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.data.usecase.GetFavorite
import com.fappslab.lorempicsumapi.data.usecase.SavePhoto
import com.fappslab.lorempicsumapi.data.usecase.SetFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ModalViewModel
@Inject
constructor(
    private val getFavorite: GetFavorite,
    private val setFavorite: SetFavorite,
    private val savePhoto: SavePhoto
) : ViewModel() {
    private val _insertEvent = MutableLiveData<DataState<Boolean>>()
    val insertEvent: LiveData<DataState<Boolean>> get() = _insertEvent

    private val _selectEvent = MutableLiveData<DataState<Photo>>()
    val selectEvent: LiveData<DataState<Photo>> get() = _selectEvent

    private val _saveEvent = MutableLiveData<DataState<Void?>>()
    val saveEvent: LiveData<DataState<Void?>> get() = _saveEvent

    fun getFavorite(id: Long) {
        viewModelScope.launch {
            _selectEvent.value = getFavorite.invoke(GetFavorite.Params(id = id))
        }
    }

    fun setFavorite(photo: Photo) {
        viewModelScope.launch {
            _insertEvent.value = setFavorite.invoke(SetFavorite.Params(photo = photo))
        }
    }

    fun savePhoto(context: Context, bitmap: Bitmap?) {
        viewModelScope.launch {
            _saveEvent.value =
                savePhoto.invoke(SavePhoto.Params(context = context, bitmap = bitmap))
        }
    }
}
