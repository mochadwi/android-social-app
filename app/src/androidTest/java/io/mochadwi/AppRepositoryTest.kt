package io.mochadwi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mochadwi.data.repository.AppRepository
import io.mochadwi.util.ext.default
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject

/**
 *
 * In syaa Allah created or modified by @mochadwi
 * On 2019-05-20 for social-app
 */

@RunWith(AndroidJUnit4::class)
class AppRepositoryTest : KoinTest {
    private val repository by inject<AppRepository>()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun test_getPostsApi() {
        runBlocking {
            val result = repository.getPostsAsync().await()
            result?.forEach(::println)
        }
    }

    @Test
    fun test_isNotEmptyPostsApi() {
        runBlocking {
            val result = repository.getPostsAsync().await()

            assertEquals(true, result != null)
            assertEquals(true, result?.isNotEmpty().default)
        }
    }
}