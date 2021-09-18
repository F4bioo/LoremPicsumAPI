package com.fappslab.lorempicsumapi.utils.extensions

import android.view.View
import android.widget.ImageView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


fun ImageView.set(imageUrl: String) {
    Glide.with(this)
        .load(imageUrl)
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .into(this)
}

fun FragmentActivity.showSystemUI(viewGroup: View) {
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(
        window, viewGroup
    ).show(WindowInsetsCompat.Type.systemBars())
}

fun FragmentActivity.hideSystemUI(viewGroup: View) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, viewGroup).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun <T> Fragment.getNavigationResult(key: String = "defKey") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(key: String = "defKey", result: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}
