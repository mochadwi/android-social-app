package io.mochadwi.util.binding

import android.annotation.SuppressLint
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import io.mochadwi.util.ext.*


/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 01/03/19
 * dedicated to build github-app
 *
 */

object CardViewBinding {
    @SuppressLint(value = ["PrivateResource", "UNCHECKED_CAST"])
    @BindingAdapter(value = ["card:isError", "card:isProgress"], requireAll = false)
    @JvmStatic
    fun CardView.isVisible(isError: Boolean?, isProgress: Boolean?) {
        if (!isError.default && !isProgress.default) {
            visible
            slideTop()
        } else {
            gone
            stopAnim()
        }
    }
}