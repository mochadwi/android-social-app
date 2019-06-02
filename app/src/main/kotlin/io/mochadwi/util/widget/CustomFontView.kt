package io.mochadwi.util.widget

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputLayout
import io.mochadwi.R

/**
 * An EditText that applies a custom font.
 *
 * @author cory@petosky.net
 */
class CustomFontView : TextInputLayout {

    constructor(context: Context) : super(context) {
        initTypeface()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initTypeface()
    }

    constructor(
            context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initTypeface()
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        if (customTypeface != null) {
            typeface = customTypeface
        }
    }

    override fun setError(error: CharSequence?) {
        var spannable: Spannable? = null
        if (!TextUtils.isEmpty(error) && error != null)
            spannable = wrapInCustomFont(error.toString())

        super.setError(spannable)
    }

    private fun initTypeface() {
        //        String fontPath = CalligraphyConfig.get().getFontPath();
        //        if (fontPath != null) {
        customTypeface = getCustomTypeface(context)
        typeface = customTypeface
        //        }
    }

    private fun wrapInCustomFont(text: String): Spannable {
        val spannable = SpannableString(text)
        spannable.setSpan(
                CustomTypefaceSpan(typeface ?: Typeface.DEFAULT),
                0,
                text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }

    /**
     * Set a custom font for our EditText.
     *
     *
     * We do this in onAttachedToWindow instead of the constructor to support
     * password input types. Internally in TextView, setting the password
     * input type overwrites the specified typeface with the system default
     * monospace.
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // Our fonts aren't present in developer tools, like live UI
        // preview in AndroidStudio.
        if (!isInEditMode) {
            typeface = typeface
        }
    }

    companion object {

        lateinit var customTypeface: Typeface

        /**
         * Load and store the custom typeface for this app.
         *
         *
         * You should have a font file in: project-root/assets/fonts/
         * or in: project-root/res/fonts/
         */
        private fun getCustomTypeface(context: Context): Typeface {
            if (!::customTypeface.isInitialized) {
                //            customTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/my_font.ttf");
                customTypeface = ResourcesCompat.getFont(context, R.font.quicksand)
                        ?: Typeface.DEFAULT
            }
            return customTypeface
        }
    }
}