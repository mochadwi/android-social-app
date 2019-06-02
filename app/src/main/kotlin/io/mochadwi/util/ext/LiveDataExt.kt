package io.mochadwi.util.ext

import androidx.lifecycle.LiveData
import io.mochadwi.util.mvvm.LiveEvent
import io.mochadwi.util.mvvm.SingleLiveEvent

/**
 * Created by mochadwi on 12/26/2018.
 */
// TODO: Use MutableLiveData to allow multiple observation?
val onSuccess = SingleLiveEvent<String>()
val onFailure = SingleLiveEvent<String>()

fun <T> LiveData<T>.toSingleEvent(): LiveData<T> {
    val result = LiveEvent<T>()
    result.addSource(this) {
        result.value = it
    }
    return result
}