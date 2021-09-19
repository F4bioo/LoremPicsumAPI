package com.fappslab.lorempicsumapi.data.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fappslab.lorempicsumapi.data.repository.LocalDataRepository
import com.fappslab.lorempicsumapi.data.state.DataState
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DeleteFavoriteTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repository: LocalDataRepository = mock()
    private lateinit var deleteFavorite: DeleteFavorite

    @Test
    fun `should return exception when remove a favorite`(): Unit = runBlocking {
        deleteFavorite = DeleteFavorite(repository)
        val dataState = deleteFavorite.invoke(DeleteFavorite.Params(-1))
        assertTrue(dataState is DataState.OnException)
    }

}