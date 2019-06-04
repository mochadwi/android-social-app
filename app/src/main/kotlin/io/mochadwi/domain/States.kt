package io.mochadwi.domain

import io.mochadwi.domain.post.PostModel

/**
 * Abstract State
 */
sealed class State

/**
 * Generic Loading State
 */
object LoadingState : State()

/**
 * Generic Error state
 * @param error - caught error
 */
data class ErrorState(val error: Throwable) : State()

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
