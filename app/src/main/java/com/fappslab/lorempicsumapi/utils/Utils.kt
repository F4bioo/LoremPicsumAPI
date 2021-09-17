package com.fappslab.lorempicsumapi.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources
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

    fun Context.share(url: String) =
        startActivity(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_photo, url))
            type = "text/plain"
            Intent.createChooser(this, getString(R.string.share_title))
        })
}
