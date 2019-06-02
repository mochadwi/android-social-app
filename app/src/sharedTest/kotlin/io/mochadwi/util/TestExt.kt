package io.mochadwi.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.mochadwi.util.ext.coroutineAsync
import org.koin.core.module.Module
import org.koin.dsl.module
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.stubbing.OngoingStubbing
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

/**
 *
 * In syaa Allah created or modified by @mochadwi
 * On 13/05/19 for social-app
 */

fun <T> LiveData<T>.blockingObserve(block: (Observer<T>) -> Unit): T? {
    var value: T? = null
    val latch = CountDownLatch(1)

    val observer = Observer<T> { t ->
        value = t
        latch.countDown()
    }

    observeForever(observer)
    block(observer)

    latch.await(2, TimeUnit.SECONDS)

    return value
}

fun <T> LiveData<T>.blockingObserveTest(block: (Observer<T>) -> Unit) {
    val latch = CountDownLatch(1)

    val observer = Observer<T> {
        latch.countDown()
    }

    observeForever(observer)
    block(observer)

    latch.await(2, TimeUnit.SECONDS)
}

fun <T> LiveData<T>.observeForTest(observe: Observer<T>, block: (Observer<T>) -> Unit) {
    val latch = CountDownLatch(1)
    try {
        latch.countDown()
        observeForever(observe)
        block(observe)
        latch.await(3, TimeUnit.SECONDS)
    } finally {
        removeObserver(observe)
    }
}

inline fun <reified T> mock() = Mockito.mock(T::class.java)
inline fun <T> whenever(methodCall: T): OngoingStubbing<T> =
        `when`(methodCall)

inline fun <reified T : Any> T.singleton(scope: KClass<out Any>? = null,
                                         autoStart: Boolean = false,
                                         override: Boolean = false
): Module = module {
    scope?.let {
        // TODO: @mochadwi add koin-androidx-scope
    } ?: single(createdAtStart = autoStart, override = override) { this@singleton }
}

//fun <T> T.toDeferred() = CompletableDeferred(this)
fun <T> T.toDeferred() = coroutineAsync { this@toDeferred }
