package io.mochadwi.util.binding

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.BindingAdapter
import io.mochadwi.util.ext.*


/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 01/03/19
 * dedicated to build github-app
 *
 */

object ViewBinding {
    @SuppressLint(value = ["PrivateResource", "UNCHECKED_CAST"])
    @BindingAdapter(value = ["view:isProgress"], requireAll = false)
    @JvmStatic
    fun View.isVisible(isProgress: Boolean?) {
        if (!isProgress.default) {
            visible
            slideTop()
        } else {
            gone
            stopAnim()
        }
    }
}