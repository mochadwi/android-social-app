package io.mochadwi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mochadwi.data.datasource.room.UserDao
import io.mochadwi.util.mock.MockedData.mockUsersEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
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
    private val userDao: UserDao by inject()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun before() {
        userDao.delete(mockUsersEntity)
    }

    @Test
    fun test_insertUserDao() = runBlocking {
        val result = userDao.insert(mockUsersEntity)
        result.forEach(::println)

        val users = userDao.getAllUsers()

        assertEquals(true, result.isNotEmpty())
        assertEquals(mockUsersEntity.count(), users.count())
        assertEquals(mockUsersEntity[0].id, users[0].id)
    }

    @Test
    fun test_getAllUsersDao() = runBlocking {
        val result = userDao.getAllUsers()
        result.forEach(::println)

        assertEquals(true, result.isNotEmpty())
    }
}