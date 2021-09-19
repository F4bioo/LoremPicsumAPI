package com.fappslab.lorempicsumapi.data.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fappslab.lorempicsumapi.data.model.Photo
import com.fappslab.lorempicsumapi.data.repository.RemoteDataRepository
import com.fappslab.lorempicsumapi.data.state.DataState
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import dev.thiagosouto.butler.file.readFile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class GetPhotosTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repository: RemoteDataRepository = mock()
    private lateinit var getPhotos: GetPhotos

    private fun errorMock(): Response<List<Photo>?> {
        val json = readFile("json/error_mock.json")
        return Response.error<List<Photo>?>(
            404,
            json.toResponseBody("application/json".toMediaTypeOrNull())
        )
    }

    @Test
    fun `should fetch result when call API frm repository`(): Unit = runBlocking {
        getPhotos = GetPhotos(repository)
        getPhotos.invoke(GetPhotos.Params(1))
        verify(repository, times(1)).getPhotos(1)
    }

    @Test
    fun `should return exception when get a null response`(): Unit = runBlocking {
        whenever(repository.getPhotos(1)).thenReturn(null)
        getPhotos = GetPhotos(repository)
        val dataState = getPhotos.invoke(GetPhotos.Params(1))
        assertTrue(dataState is DataState.OnException)
    }

    @Test
    fun `should return error when get 404 response`(): Unit = runBlocking {
        whenever(repository.getPhotos(1)).thenReturn(null)
        getPhotos = GetPhotos(repository)
        val dataState = getPhotos.invoke(GetPhotos.Params(1))
        assertTrue(dataState is DataState.OnError)
    }
}