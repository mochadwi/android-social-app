package io.mochadwi.util.binding

import android.graphics.PorterDuff
import android.os.Build
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import io.mochadwi.util.ext.*


/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 01/03/19
 * dedicated to build github-app
 *
 */

object ProgressBinding {
    @BindingAdapter("progress:customColor", requireAll = true)
    @JvmStatic
    fun ProgressBar.setCustomColor(customColor: Int) {
        // fixes pre-Lollipop progressBar indeterminateDrawable tinting
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            val wrapDrawable = DrawableCompat.wrap(this.indeterminateDrawable)
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(context, customColor))
            this.indeterminateDrawable = DrawableCompat.unwrap(wrapDrawable)
        } else {
            this.indeterminateDrawable.setColorFilter(ContextCompat.getColor(context, customColor), PorterDuff.Mode.SRC_IN)
        }
    }

    @BindingAdapter("progress:isLoading")
    @JvmStatic
    fun ProgressBar.setLoadingAnim(isLoading: Boolean?) {
        if (isLoading.default) {
            visible
            rotate()
        } else {
            gone
            stopAnim()
        }
    }
}