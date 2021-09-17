package com.fappslab.lorempicsumapi.utils.extensions

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide


fun ImageView.set(imageUrl: String) {
    Glide.with(this)
        .load(imageUrl)
        .centerCrop()
        .into(this)
}

fun <T> Fragment.getNavigationResult(key: String = "defKey") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(key: String = "defKey", result: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}
