package io.mochadwi.util.ext

import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 05/05/19
 * dedicated to build social-app
 *
 */

// Ref: https://medium.com/@sampsonjoliver/achieving-async-await-in-the-android-wasteland-a6fe30dbaaa1
fun coroutineLaunch(context: CoroutineContext = Dispatchers.Default,
                    runner: suspend CoroutineScope.() -> Unit): Job =
        CoroutineScope(context).launch { runner.invoke((this)) }

fun <T> coroutineAsync(context: CoroutineContext = Dispatchers.Default,
                       runner: suspend CoroutineScope.() -> T): Deferred<T> =
        CoroutineScope(context).async { runner.invoke((this)) }

suspend fun <T> CoroutineScope.await(block: () -> Deferred<T>): T = block().await()

suspend inline fun <reified T> CoroutineScope.awaitAsync(context: CoroutineContext = Dispatchers.Default,
                                                         crossinline block: () -> T): T =
        withContext(context) { block() }

suspend fun <T> asyncTask(function: () -> T): T {
    return withContext(Dispatchers.Default) { function() }
}

fun <T> lazyPromiseLocal(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        CoroutineScope(Dispatchers.Default)
                .async(start = CoroutineStart.LAZY) { block.invoke(this) }
    }
}

fun <T> lazyPromiseGlobal(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> {
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}

// TODO: firebase or gms related task to be coroutine ready
//fun <T> Task<T>.asDeferred(): Deferred<T> {
//    val deferred = CompletableDeferred<T>()
//
//    deferred.invokeOnCompletion {
//        if (deferred.isCancelled) {
//            // optional, handle coroutine cancellation however you'd like here
//        }
//    }
//
//    this.addOnSuccessListener { result -> deferred.complete(result) }
//    this.addOnFailureListener { exception -> deferred.completeExceptionally(exception) }
//
//    return deferred
//}

/*
suspend inline fun <reified T> Task<T>.await(): T = suspendCoroutine { continuation ->
    addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(task.result as T)
        } else {
            continuation.resumeWithException(task.exception
                    ?: RuntimeException("Unknown task exception"))
        }
    }
}
*/

suspend inline fun File?.await(): File = suspendCoroutine { cont ->
    if (this != null) cont.resume(this)
    else cont.resumeWithException(RuntimeException("Unknown file"))
}