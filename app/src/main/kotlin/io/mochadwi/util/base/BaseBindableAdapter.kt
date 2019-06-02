package io.mochadwi.util.base

interface BaseBindableAdapter<in T> {
    fun setHeader(items: T) {}
    fun setData(items: List<T>) {}
    fun setFooter(items: T) {}
    fun bind(data: T) {}
}