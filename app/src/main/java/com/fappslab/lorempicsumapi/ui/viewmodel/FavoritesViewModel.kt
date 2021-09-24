package com.fappslab.lorempicsumapi.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fappslab.lorempicsumapi.data.api.DataState
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.usecase.DeleteFavorite
import com.fappslab.lorempicsumapi.data.usecase.GetFavorites
import com.fappslab.lorempicsumapi.data.usecase.SetFavorite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel
@Inject
constructor(
    private val getFavorites: GetFavorites,
    private val setFavorite: SetFavorite,
    private val deleteFavorite: DeleteFavorite,
) : ViewModel() {
    private val _getFavoritesEvent = MutableLiveData<DataState<List<Photo>>>()
    val getFavoritesEvent: LiveData<DataState<List<Photo>>>
        get() = _getFavoritesEvent

    private val _setFavoritesEvent = MutableLiveData<DataState<Boolean>>()
    val setFavoritesEvent: LiveData<DataState<Boolean>>
        get() = _setFavoritesEvent

    private val _deleteEvent = MutableLiveData<DataState<Boolean>>()
    val deleteEvent: LiveData<DataState<Boolean>>
        get() = _deleteEvent

    fun getFavorites() {
        viewModelScope.launch {
            _getFavoritesEvent.value = getFavorites.invoke()
        }
    }

    fun setFavorite(photo: Photo) {
        viewModelScope.launch {
            _setFavoritesEvent.value =  setFavorite.invoke(SetFavorite.Params(photo))
        }
    }

    fun deleteFavorite(id: Long) {
        viewModelScope.launch {
            _deleteEvent.value = deleteFavorite.invoke(DeleteFavorite.Params(id = id))
        }
    }
}
