package io.mochadwi.mock.mvvm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mochadwi.data.repository.AppRepository
import io.mochadwi.di.testOnlineSocialApp
import io.mochadwi.domain.ErrorState
import io.mochadwi.domain.LoadingState
import io.mochadwi.domain.State
import io.mochadwi.util.MockitoHelper.argumentCaptor
import io.mochadwi.util.TestSchedulerProvider
import io.mochadwi.util.mock.MockedData.mockUserQuery
import io.mochadwi.util.mock.MockedData.mockUsername
import io.mochadwi.util.mock.MockedData.mockUsersModel
import io.mochadwi.util.toDeferred
import io.mochadwi.view.user.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build weather-app
 *
 */
@ExperimentalCoroutinesApi
class UserViewModelMockTest : KoinTest {

    @Mock
    lateinit var repository: AppRepository
    val viewModel: UserViewModel by lazy {
        UserViewModel(repository, TestSchedulerProvider())
    }

    @Mock
    lateinit var statesView: Observer<State>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    val times = 3

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        startKoin { modules(testOnlineSocialApp) }
        viewModel.states.observeForever(statesView)
    }

    @After
    fun after() {
        stopKoin()
        viewModel.states.removeObserver(statesView)
    }

    @Test
    fun `test CategoryViewModel getUserByKeyword Succeed`() = runBlockingTest {
        val page = 1
        given(repository.getUsersAsync(mockUserQuery)).willReturn(mockUsersModel.toDeferred())

        viewModel.getUserByKeyword(mockUsername, page)

        // setup ArgumentCaptor
        val arg = argumentCaptor<State>()
        // Here we expect $times calls on statesView.onChanged
        verify(statesView, times(2)).onChanged(arg.capture())

        val values = arg.allValues
        // Test obtained values in order
        assertEquals(2, values.size)
        assertEquals(LoadingState, values[0])
        assertEquals(UserViewModel.UserListState.from(page == 1, false, mockUsersModel), values[1])
    }

    @Test
    fun `test CategoryViewModel getUserByKeyword Failed`() = runBlockingTest {
        val error = Throwable("got an error")
        given(repository.getUsersAsync(mockUserQuery)).will { throw error }

        viewModel.getUserByKeyword(mockUsername, 1)

        // setup ArgumentCaptor
        val arg = argumentCaptor<State>()
        // Here we expect 2 calls on statesView.onChanged
        verify(statesView, times(2)).onChanged(arg.capture())

        val values = arg.allValues
        // Test obtained values in order
        assertEquals(2, values.size)
        assertEquals(LoadingState, values[0])
        assertEquals(ErrorState(error), values[1])
    }
}
