package io.mochadwi.util.mvvm

import androidx.databinding.ObservableField

/**
 * Created by mochadwi on 9/6/18.
 */
open class MutableSetObservableField<T>(private val list: MutableSet<T> = mutableSetOf()) :
        MutableSet<T> by list,
        ObservableField<MutableSet<T>>() {

    override fun add(element: T): Boolean =
            element.actionAndUpdate { list.add(it) }

    override fun addAll(elements: Collection<T>): Boolean =
            elements.actionAndUpdate { list.addAll(elements) }

    override fun remove(element: T): Boolean =
            element.actionAndUpdate { list.remove(it) }

    override fun removeAll(elements: Collection<T>): Boolean =
            elements.actionAndUpdate { list.removeAll(it) }

    override fun retainAll(elements: Collection<T>): Boolean =
            elements.actionAndUpdate { list.retainAll(it) }

    override fun clear() =
            list.clear().also { updateValue() }

    private fun <T> T.actionAndUpdate(action: (item: T) -> Boolean): Boolean =
            action(this).applyIfTrue { updateValue() }

    private fun Boolean.applyIfTrue(action: () -> Unit): Boolean {
        takeIf { it }?.run { action() }
        return this
    }

    private fun updateValue() {
        set(list)
        notifyChange()
    }
}