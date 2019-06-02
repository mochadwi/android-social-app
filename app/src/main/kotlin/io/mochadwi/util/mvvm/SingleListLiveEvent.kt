package io.mochadwi.util.mvvm

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by mochadwi on 8/09/18.
 */
@Deprecated(
        "Use @see io.mochadwi.util.mvvm#ListLiveEvent instead to observe a list of events.",
        ReplaceWith("ListLiveEvent", "io.mochadwi.util.mvvm#ListLiveEvent")
)
class SingleListLiveEvent<T> : MutableListLiveData<T>() {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in MutableList<T>>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer<MutableList<T>> {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    @MainThread
    override fun setValue(value: MutableList<T>?) {
        pending.set(true)
        super.setValue(value)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = mutableListOf()
    }

    // TODO: Change tag using canonnical name
    companion object {
        private const val TAG = "SingleListLiveEvent"
    }
}