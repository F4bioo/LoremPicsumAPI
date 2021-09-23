package com.fappslab.lorempicsumapi.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fappslab.lorempicsumapi.data.api.DataState
import com.fappslab.lorempicsumapi.data.usecase.SavePhoto
import com.fappslab.lorempicsumapi.utils.Constants
import com.fappslab.lorempicsumapi.utils.Prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
@Inject
constructor(
    private val savePhoto: SavePhoto,
    private val prefs: Prefs
) : ViewModel() {

    private val _saveEvent = MutableLiveData<DataState<Void?>>()
    val saveEvent: LiveData<DataState<Void?>> get() = _saveEvent

    fun savePhoto(context: Context, bitmap: Bitmap?) {
        viewModelScope.launch {
            _saveEvent.value =
                savePhoto.invoke(SavePhoto.Params(context = context, bitmap = bitmap))
        }
    }

    fun hasModalOpened(): Boolean {
        return prefs.getBoolean(
            Constants.KEY_HAS_MODAL_OPENED, false
        ).apply {
            prefs.setBoolean(Constants.KEY_HAS_MODAL_OPENED, true)
        }
    }
}
