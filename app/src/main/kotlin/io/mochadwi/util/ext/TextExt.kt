package io.mochadwi.util.ext

import android.text.SpannableStringBuilder
import kotlinx.serialization.serializer
import java.text.DecimalFormat

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 13/01/19
 * dedicated to build github-app
 *
 */

// TODO: e.g: "string".putSpans(howmany, start, end, flags) { ForgroundColorSpan(color), StyleSpan(BOLD) }
fun String.putSpans(vararg flags: Int.() -> Unit, spanBuilder: SpannableStringBuilder.() -> Unit):
        SpannableStringBuilder = SpannableStringBuilder(this).apply(spanBuilder).apply {
    //    while ()
}

fun Int.convertToIDR(): String {
    val formatter = DecimalFormat("###,###,###")
    return "Rp " + formatter.format(java.lang.Double.parseDouble(this.toString()))
}

fun Any.convertToString(): String {
    return this.toJson(Any::class.serializer())
}

/*
@ExperimentalContracts
@Suppress("UNUSED_PARAMETER")
fun Int?.isNullOrBlank(): Boolean {
    contract {
        returns(false) implies (this@isNullOrBlank != null)
    }

    return this == null || this.isBlank()
}
*/

// Assume default value is -1, flagged it as blank, unless
fun Int.isBlank(): Boolean {
    return this == -1
}

fun Int?.isNullOrZero(): Boolean = this == null || this.isZero()

fun Int.isZero(): Boolean = this == 0