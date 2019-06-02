package io.mochadwi.domain

import android.os.Parcelable

/**
 * Abstract Event from ViewModel
 */
open class Event

/**
 * Generic Loading Event
 */
object LoadingEvent : Event()

/**
 * Generic Success Event
 */
object SuccessEvent : Event()

/**
 * Generic Failed Event
 */
data class FailedEvent(val error: Throwable) : Event()

data class HttpEvent(val code: Int, val message: String) : Event()

data class ResultEvent(val data: String) : Event()

data class ResultsEvent<T : Parcelable>(val data: T) : Event()