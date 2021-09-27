package com.fappslab.lorempicsumapi.data.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class GetPhotosTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `should fetch result when call API frm repository`(): Unit = runBlocking {

    }

    @Test
    fun `should return exception when get a null response`(): Unit = runBlocking {

    }

    @Test
    fun `should return error when get 404 response`(): Unit = runBlocking {

    }

    @Test
    fun `should return success when get response`(): Unit = runBlocking {

    }
}
