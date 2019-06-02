package io.mochadwi.util.widget

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

import com.google.android.material.textfield.TextInputLayout

/**
 * Created by @mochadwi
 * TextInputLayout for use with the default Calligraphy font
 */
class CalligraphyTextInputLayout : TextInputLayout {

    internal var calligraphyTypeface: Typeface? = null

    constructor(context: Context) : super(context) {
        initCalligraphyTypeface()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initCalligraphyTypeface()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initCalligraphyTypeface()
    }


    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        if (calligraphyTypeface != null) {
            typeface = calligraphyTypeface
        }
    }

    override fun setError(error: CharSequence?) {
        var spannable: Spannable? = null
        if (!TextUtils.isEmpty(error) || error != null)
            spannable = wrapInCustomFont(error!!.toString())

        super.setError(spannable)
    }

    private fun initCalligraphyTypeface() {
        //        String fontPath = CalligraphyConfig.get().getFontPath();
        //        if (fontPath != null) {
        //            calligraphyTypeface = TypefaceUtils.load(getResources().getAssets(), fontPath);
        //        }
    }

    private fun wrapInCustomFont(text: String): Spannable {
        //        CalligraphyTypefaceSpan typefaceSpan = new CalligraphyTypefaceSpan(getTypeface());
        //        spannable.setSpan(typefaceSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return SpannableString(text)
    }
}
