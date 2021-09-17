package com.fappslab.lorempicsumapi.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.data.usecase.GetFavorites
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel
@Inject
constructor(
    private val getFavorites: GetFavorites
) : ViewModel() {
    private val _getFavoriteEvent = MutableLiveData<DataState<List<Photo>>>()
    val getFavoriteEvent: LiveData<DataState<List<Photo>>> get() = _getFavoriteEvent

    fun getFavorites() {
        viewModelScope.launch {
            _getFavoriteEvent.value = getFavorites.invoke()
        }
    }
}
