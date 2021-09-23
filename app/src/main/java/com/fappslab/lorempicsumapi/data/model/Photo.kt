package com.fappslab.lorempicsumapi.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: String,
    val author: String,
    val width: String,
    val height: String,
    val url: String,
    val downloadUrl: String,
    var favorite: Boolean = false
) : Parcelable
