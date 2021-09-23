package com.fappslab.lorempicsumapi.data.model

import com.google.gson.annotations.SerializedName

data class PhotoDomain(
    @SerializedName("id")
    val id: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("width")
    val width: String,
    @SerializedName("height")
    val height: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("download_url")
    val downloadUrl: String
)
