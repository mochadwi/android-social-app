package io.mochadwi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mochadwi.data.datasource.room.PostDao
import io.mochadwi.util.mock.MockedData.mockPostsEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build social-app
 *
 */

/**
 * AppDaoTest is a KoinTest with AndroidJUnit4 runner
 *
 * KoinTest help inject Koin components from actual runtime
 */
@RunWith(AndroidJUnit4::class)
class AppDaoTest : KoinTest {

    /*
     * Inject needed components from Koin
     */
    private val postDao: PostDao by inject()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun test_insertPostDao() = runBlocking {
        val result = postDao.insert(mockPostsEntity)
        result.forEach(::println)

        val posts = postDao.getAllPosts()

        assertEquals(true, result.isNotEmpty())
        assertEquals(mockPostsEntity.count(), posts.count())
        assertEquals(mockPostsEntity[0].id, posts[0].id)
    }

    @Test
    fun test_getAllPostsDao() = runBlocking {
        val result = postDao.getAllPosts()
        result.forEach(::println)

        assertEquals(true, result.isNotEmpty())
    }

    @Test
    fun test_searchPostsDao() = runBlocking {
        val result = postDao.searchPosts("foo")
        result.forEach(::println)

        assertEquals(true, result.isNotEmpty())
    }
}