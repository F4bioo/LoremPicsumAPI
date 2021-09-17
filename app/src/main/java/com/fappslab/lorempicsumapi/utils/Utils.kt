package com.fappslab.lorempicsumapi.utils

import android.content.res.Resources
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.fappslab.lorempicsumapi.R

object Utils {
    private val width = { Resources.getSystem().displayMetrics.widthPixels }
    private val height = { Resources.getSystem().displayMetrics.heightPixels }

    fun normalUrl(id: String, width: Int = width(), height: Int = height()): String {
        return "https://picsum.photos/id/$id/$width/$height"
    }

    fun greyscaleUrl(id: String, width: Int = width(), height: Int = height()): String {
        return "https://picsum.photos/id/$id/$width/$height?grayscale"
    }

    fun blurUrl(id: String, width: Int = width(), height: Int = height()): String {
        return "https://picsum.photos/id/$id/$width/$height?blur=10"
    }
}
