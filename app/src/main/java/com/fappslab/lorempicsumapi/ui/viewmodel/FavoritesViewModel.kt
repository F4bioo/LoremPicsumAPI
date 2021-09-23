package com.fappslab.lorempicsumapi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fappslab.lorempicsumapi.data.api.DataState
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.usecase.DeleteFavorite
import com.fappslab.lorempicsumapi.data.usecase.GetFavorites
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel
@Inject
constructor(
    private val getFavorites: GetFavorites,
    private val deleteFavorite: DeleteFavorite,
) : ViewModel() {
    private val _getFavoritesEvent = MutableLiveData<DataState<List<Photo>>>()
    val getFavoritesEvent: LiveData<DataState<List<Photo>>> get() = _getFavoritesEvent

    private val _deleteEvent = MutableLiveData<DataState<Boolean>>()
    val deleteEvent: LiveData<DataState<Boolean>> get() = _deleteEvent

    fun getFavorites() {
        viewModelScope.launch {
            _getFavoritesEvent.value = getFavorites.invoke()
        }
    }

    fun deleteFavorite(id: Long) {
        viewModelScope.launch {
            _deleteEvent.value = deleteFavorite.invoke(DeleteFavorite.Params(id = id))
        }
    }
}
