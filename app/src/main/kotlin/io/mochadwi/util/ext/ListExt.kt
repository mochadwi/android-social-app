package io.mochadwi.util.ext

import android.view.View
import androidx.recyclerview.widget.*

fun RecyclerView.setupLinearLayoutManager(orientation: Int, isReversed: Boolean = false) {
    addItemDecoration(DividerItemDecoration(context,
            if (orientation == 1) RecyclerView.HORIZONTAL
            else RecyclerView.VERTICAL
    ))
    layoutManager = LinearLayoutManager(
            context,
            if (orientation == 1) RecyclerView.HORIZONTAL
            else RecyclerView.VERTICAL,
            isReversed)
    setHasFixedSize(true)
    itemAnimator = DefaultItemAnimator()
    setItemViewCacheSize(30)
    isDrawingCacheEnabled = true
    drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
}

fun RecyclerView.setupGridLayoutManager(span: Int, orientation: Int, isReversed: Boolean) {
    layoutManager = GridLayoutManager(context, span)
    setHasFixedSize(true)
    itemAnimator = DefaultItemAnimator()
    setItemViewCacheSize(30)
    isDrawingCacheEnabled = true
    drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
}

//https://stackoverflow.com/questions/52891684/shift-kotlin-array
fun <T> Array<T>.leftShift(d: Int) {
    val n = d % this.size  // just in case
    if (n == 0) return  // no need to shift

    val left = this.copyOfRange(0, n)
    val right = this.copyOfRange(n, this.size)
    System.arraycopy(right, 0, this, 0, right.size)
    System.arraycopy(left, 0, this, right.size, left.size)
}