package com.fappslab.lorempicsumapi.utils

import org.junit.Assert.assertTrue
import org.junit.Test

class UtilsTest {

    @Test
    fun `should return a normal url image`() {
        val url = Utils.normalUrl("145", 300, 300)
        assertTrue(url == "https://picsum.photos/id/145/300/300")
    }

    @Test
    fun `should return a greyscale url image`() {
        val url = Utils.greyscaleUrl("145", 300, 300)
        assertTrue(url == "https://picsum.photos/id/145/300/300?grayscale")
    }

    @Test
    fun `should return a blur url image`() {
        val url = Utils.blurUrl("145", 300, 300)
        assertTrue(url == "https://picsum.photos/id/145/300/300?blur=10")
    }

}