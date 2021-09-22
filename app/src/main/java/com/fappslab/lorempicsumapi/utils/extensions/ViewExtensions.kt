package com.fappslab.lorempicsumapi.utils.extensions

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.DrawableRes
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.button.MaterialButton
import java.util.*


fun ImageView.set(imageUrl: String) {
    Glide.with(this)
        .load(imageUrl)
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .into(this)
}

fun ImageView.bg(progress: ProgressBar?) {
    Random().apply {
        val color = Color.argb(
            125,
            nextInt(256),
            nextInt(256),
            nextInt(256)
        )
        setBackgroundColor(color)
        progress?.indeterminateTintList =
            ColorStateList.valueOf(color)
    }
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

fun MaterialButton.set(@DrawableRes icon: Int, color: Int) {
    setIconResource(icon)
    iconTint = ColorStateList.valueOf(color)
}

fun MaterialButton.set(@DrawableRes icon: Int) {
    setIconResource(icon)
}

fun <T> Fragment.getNavigationResult(key: String = "defKey") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(key: String = "defKey", result: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}
