package io.mochadwi.util.binding

import android.graphics.Typeface
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import io.mochadwi.R
import io.mochadwi.util.ext.default
import io.mochadwi.util.ext.putSpans
import kotlin.math.ceil

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 13/01/19
 * dedicated to build social-app
 *
 */

object TextBinding {
    @BindingAdapter("app:noColorText", "app:coloredText", "app:whatColor", requireAll = true)
    @JvmStatic
    fun TextView.setMultiColorText(noColorText: String?, coloredText: String?, whatColor: Int?) {
        context.apply {
            if (noColorText != null && coloredText != null) {
                // TODO: Change SpannableBuilder to be modular (without, reinput duplicate args)
                val resultSpan = "$noColorText$coloredText ".putSpans {
                    setSpan(ForegroundColorSpan(ContextCompat.getColor(
                            context,
                            whatColor ?: android.R.color.black)),
                            noColorText.length, this.length, // this is duplicate
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE) // this is duplicate
                    setSpan(StyleSpan(Typeface.BOLD),
                            noColorText.length, this.length, // this is duplicate
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE) // this is duplicate
                }
                text = resultSpan
            }
        }
    }

    @BindingAdapter("text:price", requireAll = true)
    @JvmStatic
    fun EditText.setPriceTag(price: String) {
        context.apply {
            setText(String.format("IDR %s", price))
        }
    }

    // TODO: Build inverse binding adapter: https://medium.com/@douglas.iacovelli/custom-two-way-databinding-made-easy-f8b17a4507d2
    @BindingAdapter("text:price", requireAll = true)
    @JvmStatic
    fun AppCompatEditText.setPriceTag(price: String) {
        context.apply {
            //            is
            setText(String.format("IDR %s", price))
        }
    }

    @BindingAdapter("text:price", requireAll = true)
    @JvmStatic
    fun TextView.setPriceTag(price: String) {
        context.apply {
            text = String.format("$ %s", price)
        }
    }

    @BindingAdapter("text:score", requireAll = true)
    @JvmStatic
    fun TextView.setScoreTag(score: Double) {
        context.apply {
            text = String.format("Score: %s", ceil(score))
        }
    }

    @BindingAdapter("text:id", "text:username", requireAll = true)
    @JvmStatic
    fun TextView.setScoreTag(id: Int, username: String) {
        context.apply {
            text = if (id == -1) "Next result will be limited"
            else String.format("Username: %s", username)
        }
    }

    // TODO: Build see more/less on product list item, implement inversebinding https://stackoverflow.com/questions/52389235/using-calendarview-with-databinding/52402099#52402099
    // TODO: https://stackoverflow.com/questions/52223335/android-two-way-data-binding-doesnt-work-with-edittext-and-observable-variable
    @BindingAdapter("text:visibility", requireAll = true)
    @JvmStatic
    fun TextView.setVisibility(visibility: Boolean) {
        context.apply {
            text =
                    if (!visibility) String.format("%s", "Lihat lebih lanjut >>")
                    else String.format("%s", "<< Lihat lebih sedikit")
        }
    }

    @BindingAdapter("text:celcius", requireAll = true)
    @JvmStatic
    fun TextView.setCelcius(celcius: String?) {
        context.apply {
            text = getString(R.string.message_celcius, celcius.default)
        }
    }

    @BindingAdapter("text:celciusSymbol", requireAll = true)
    @JvmStatic
    fun TextView.setCelciusSymbol(celciusSymbol: String?) {
        context.apply {
            text = getString(R.string.message_celcius_symbol, celciusSymbol.default)
        }
    }

    @BindingAdapter("text:errorMsg", requireAll = true)
    @JvmStatic
    fun TextView.setErrorMsg(errorMsg: String?) {
        context.apply {
            text =
                    if (errorMsg.isNullOrBlank()) getString(R.string.error_backend)
                    else errorMsg
        }
    }

}