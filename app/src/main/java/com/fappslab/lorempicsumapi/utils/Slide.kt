package com.fappslab.lorempicsumapi.utils

import android.view.animation.TranslateAnimation

object Slide {
    fun animation(duration: Long = 500, fromYDelta: Float, toYDelta: Float): TranslateAnimation {
        return TranslateAnimation(0f, 0f, fromYDelta, toYDelta).apply {
            this.duration = duration
            this.fillAfter = true
        }
    }
}
