package com.fappslab.lorempicsumapi.data.usecase

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.fappslab.lorempicsumapi.data.state.DataState
import com.fappslab.lorempicsumapi.utils.BaseUseCase
import com.fappslab.lorempicsumapi.utils.Constants
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class SavePhoto
@Inject
constructor() : BaseUseCase.Params<DataState<Void?>, SavePhoto.Params> {
    override suspend fun invoke(params: Params): DataState<Void?> {
        return try {
            saveImage(params.context, params.bitmap)
            DataState.OnSuccess(null)

        } catch (e: Exception) {
            DataState.OnException(e)
        }
    }

    data class Params(
        val context: Context,
        val bitmap: Bitmap?
    )

    private fun saveImage(context: Context, bitmap: Bitmap?) {
        val image = "${Constants.IMAGE_FILE}${System.currentTimeMillis()}.jpg"

        val stream = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, image)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)?.let {
                resolver.openOutputStream(it)
            }
        } else {
            val parent =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val file = File(parent.toString(), image)
            FileOutputStream(file)
        }

        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream?.close()
    }
}
