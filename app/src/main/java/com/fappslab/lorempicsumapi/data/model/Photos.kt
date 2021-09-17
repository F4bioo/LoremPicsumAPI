package com.fappslab.lorempicsumapi.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photos(
    val photos: List<Photo>
) : Parcelable
