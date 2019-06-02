package io.mochadwi.util.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import io.mochadwi.R

/**
 * An EditText that applies a custom font.
 *
 * @author cory@petosky.net
 */

/**
 * An EditText that applies a custom font.
 *
 * @author cory@petosky.net
 */
class ItalicEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(
            context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
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
            setTypeface(getTypeface(context), Typeface.ITALIC)
        }
    }

    companion object {

        private var customTypeface: Typeface? = null

        /**
         * Load and store the custom typeface for this app.
         *
         *
         * You should have a font file in: project-root/assets/fonts/
         */
        private fun getTypeface(context: Context): Typeface {
            if (customTypeface == null) {
                customTypeface = ResourcesCompat.getFont(context, R.font.quicksand_book_oblique)
            }
            return customTypeface!!
        }
    }
}