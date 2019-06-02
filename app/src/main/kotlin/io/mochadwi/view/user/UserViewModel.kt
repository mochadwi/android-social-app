package io.mochadwi.view.user

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import io.mochadwi.data.datasource.webservice.param.QueryParam
import io.mochadwi.data.repository.AppRepository
import io.mochadwi.domain.ErrorState
import io.mochadwi.domain.LoadingState
import io.mochadwi.domain.State
import io.mochadwi.domain.user.UserModel
import io.mochadwi.util.base.BaseViewModel
import io.mochadwi.util.ext.toQueryMap
import io.mochadwi.util.ext.toSingleEvent
import io.mochadwi.util.mvvm.LiveEvent
import io.mochadwi.util.mvvm.MutableSetObservableField
import io.mochadwi.util.rx.SchedulerProvider
import io.mochadwi.view.user.list.UserItem

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build social-app
 *
 */

class UserViewModel(
        private val appRepository: AppRepository,
        schedulerProvider: SchedulerProvider
) : BaseViewModel(schedulerProvider) {

    val keywords = ObservableField<String>("")
    var userListSet = MutableSetObservableField<UserItem>()

    /*
     * We use LiveEvent to publish "states"
     * No need to publish and retain any view state
     */
    private val _states = LiveEvent<State>()
    val states: LiveData<State>
        get() = _states.toSingleEvent()

    fun getUserByKeyword(keyword: String, page: Int = 1) {
        if (page == 1) _states.value = LoadingState

        launch {
            try {
                val users = appRepository.getUsersAsync(
                        if (keyword.isNotBlank()) QueryParam(q = keyword, page = page).toQueryMap()
                        else null
                ).await()

                val isLocal = keyword.isBlank()

                _states.value = UserListState.from(
                        page == 1,
                        isLocal,
                        users!!.sortedBy { it.login })
            } catch (error: Throwable) {
                _states.value = ErrorState(error)
            }
        }
    }

    data class UserListState(
            val isFirst: Boolean,
            val isLocal: Boolean,
            val list: List<UserModel>
    ) : State() {
        companion object {
            fun from(isFirst: Boolean, isLocal: Boolean, list: List<UserModel>): UserListState {
                return with(list) {
                    when {
                        // TODO: @mochadwi Move this into strings instead
                        isEmpty() && isFirst -> error("There's an empty user instead, please check your keyword")
                        else -> UserListState(isFirst = isFirst, isLocal = isLocal, list = this)
                    }
                }
            }
        }
    }
}