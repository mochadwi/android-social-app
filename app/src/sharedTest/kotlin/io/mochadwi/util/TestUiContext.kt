package io.mochadwi.util

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Delay
import kotlinx.coroutines.InternalCoroutinesApi
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

@InternalCoroutinesApi
class TestUiContext : CoroutineDispatcher(), Delay {
//    override fun scheduleResumeAfterDelay(time: Long, unit: TimeUnit, continuation: CancellableContinuation<Unit>) {
//        continuation.resume(Unit)
//    }

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        continuation.resume(Unit)
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        //CommonPool.dispatch(context, block)  // dispatch on CommonPool
        block.run()  // dispatch on calling thread
    }
}