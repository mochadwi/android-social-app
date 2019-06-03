package io.mochadwi.view.post

import androidx.lifecycle.LiveData
import io.mochadwi.data.repository.AppRepository
import io.mochadwi.domain.ErrorState
import io.mochadwi.domain.LoadingState
import io.mochadwi.domain.State
import io.mochadwi.domain.post.PostModel
import io.mochadwi.util.base.BaseViewModel
import io.mochadwi.util.ext.toSingleEvent
import io.mochadwi.util.mvvm.LiveEvent
import io.mochadwi.util.mvvm.MutableSetObservableField
import io.mochadwi.util.rx.SchedulerProvider
import io.mochadwi.view.post.list.PostItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 10/05/19
 * dedicated to build social-app
 *
 */

class PostViewModel(
        private val appRepository: AppRepository,
        schedulerProvider: SchedulerProvider
) : BaseViewModel(schedulerProvider) {

    val keywords = Channel<String>(UNLIMITED)
    var postListSet = MutableSetObservableField<PostItem>()

    /*
     * We use LiveEvent to publish "states"
     * No need to publish and retain any view state
     */
    private val _states = LiveEvent<State>()
    val states: LiveData<State>
        get() = _states.toSingleEvent()

    fun getPosts() {
        _states.value = LoadingState

        launch {
            try {
                val posts = appRepository.getPostsAsync().await()

                _states.value = PostListState.from(false, posts!!)
            } catch (error: Throwable) {
                _states.value = ErrorState(error)
            }
        }
    }

    fun searchPosts(query: String) {
        _states.value = LoadingState

        launch {
            try {
                val posts = appRepository.searchPostsAsync(query).await()

                _states.value = PostListState.from(query.isNotBlank(), posts!!)
            } catch (error: Throwable) {
                _states.value = ErrorState(error)
            }
        }
    }

    data class PostListState(
            val isSearch: Boolean,
            val list: List<PostModel>
    ) : State() {
        companion object {
            fun from(isSearch: Boolean, list: List<PostModel>): PostListState {
                return with(list) {
                    when {
                        // TODO: @mochadwi Move this into strings instead
                        isEmpty() -> error("There's an empty post instead, please check your keyword")
                        else -> PostListState(isSearch, this)
                    }
                }
            }
        }
    }
}