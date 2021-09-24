package com.fappslab.lorempicsumapi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fappslab.lorempicsumapi.data.api.DataState
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.usecase.GetFavorite
import com.fappslab.lorempicsumapi.data.usecase.SetFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ModalViewModel
@Inject
constructor(
    private val getFavorite: GetFavorite,
    private val setFavorite: SetFavorite
) : ViewModel() {
    private val _insertEvent = MutableLiveData<DataState<Boolean>>()
    val insertEvent: LiveData<DataState<Boolean>>
        get() = _insertEvent

    private val _selectEvent = MutableLiveData<DataState<Photo>>()
    val selectEvent: LiveData<DataState<Photo>>
        get() = _selectEvent

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
}
