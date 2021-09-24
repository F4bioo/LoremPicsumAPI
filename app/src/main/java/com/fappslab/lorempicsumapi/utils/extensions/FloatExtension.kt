package com.fappslab.lorempicsumapi.utils.extensions

import android.content.res.Resources

val Float.dp: Float
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f)