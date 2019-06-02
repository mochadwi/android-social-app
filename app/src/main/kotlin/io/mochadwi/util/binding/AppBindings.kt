package io.mochadwi.util.binding

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.DEFAULT
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.InputType
import android.text.Spannable
import android.text.Spanned
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.mochadwi.R
import io.mochadwi.util.ext.putSpans
import io.mochadwi.util.helper.AppHelper
import io.mochadwi.util.helper.AppHelper.Const.BASE_IMAGE_URL_MOVIE_DB
import io.mochadwi.util.helper.AppHelper.Const.CURRENCY_VALUE_DEFAULT
import io.mochadwi.util.helper.AppHelper.Const.GLIDE_FADE_ANIMATION_TIME_DEFAULT
import io.mochadwi.util.helper.AppHelper.Const.WEBVIEW_TEXT_SIZE_DEFAULT
import io.mochadwi.util.helper.GlideApp
import io.mochadwi.util.mvvm.RxViewModel
import io.mochadwi.util.widget.CustomTypefaceSpan
import java.io.File


object AppBindings {

    @BindingAdapter("app:progressColor")
    @JvmStatic
    fun setProgressColor(loader: ProgressBar?, color: Int?) {
        loader?.indeterminateDrawable?.setColorFilter(color
                ?: 0, android.graphics.PorterDuff.Mode.SRC_IN)
    }

    @BindingAdapter("app:activeColor")
    @JvmStatic
    fun setBackgroundColorItemList(view: View, activeColor: Int?) {
        view.setBackgroundColor(activeColor ?: 0)
    }

    @BindingAdapter("app:currencyValue")
    @JvmStatic
    fun setCurrenyFormatToRupiah(textView: TextView, currencyValue: Double?) {
        textView.text = AppHelper.Func.currencyFormatToRupiah(currencyValue
                ?: CURRENCY_VALUE_DEFAULT)
    }

    @SuppressLint("PrivateResource")
    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun ImageView.setImageUrl(imageUrl: String?) {
        Log.d("LOREM ", imageUrl.toString())
        GlideApp.with(this.context)
                .load(BASE_IMAGE_URL_MOVIE_DB + imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade(
                        GLIDE_FADE_ANIMATION_TIME_DEFAULT
                ))
                .into(this)
                .apply {
                    RequestOptions()
                            .fallback(R.color.material_grey_300)
                }
    }

    @SuppressLint("PrivateResource")
    @BindingAdapter(value = ["app:imageLocal", "app:isCircle"], requireAll = false)
    @JvmStatic
    fun ImageView.setImageLocal(imageLocal: String?, isCircle: Boolean) {
        try {
            Log.d("IMAGE-RESOURCES ", imageLocal)
        } catch (e: Exception) {
        }

        GlideApp.with(context)
                .load(imageLocal)
                .transition(DrawableTransitionOptions.withCrossFade(
                        GLIDE_FADE_ANIMATION_TIME_DEFAULT))
                .apply { if (isCircle) apply(RequestOptions.circleCropTransform()) }
                .error(R.drawable.ic_account_box_black_24dp)
                .into(this)
                .apply { RequestOptions().fallback(R.color.material_grey_300) }
    }

    @SuppressLint("PrivateResource")
    @BindingAdapter(value = ["app:imageLocal", "app:isCircle"], requireAll = false)
    @JvmStatic
    fun ImageView.setImageLocal(imageLocal: File?, isCircle: Boolean) {
        try {
            Log.d("IMAGE-RESOURCES ", imageLocal?.toString())
        } catch (e: Exception) {
        }

        GlideApp.with(context)
                .load(imageLocal)
                .apply {
                    if (isCircle) with(this) {
                        apply(RequestOptions.circleCropTransform())
                        /*error(R.drawable.bg_profile)*/
                    }
                    transition(DrawableTransitionOptions.withCrossFade(
                            GLIDE_FADE_ANIMATION_TIME_DEFAULT))
                }
                .into(this)
                .apply { RequestOptions().fallback(R.color.material_grey_300) }
    }

    @SuppressLint("PrivateResource")
    @BindingAdapter(value = ["app:imageLocal", "app:isCircle"], requireAll = false)
    @JvmStatic
    fun ImageView.setImageLocal(imageLocal: Uri?, isCircle: Boolean) {
        try {
            Log.d("IMAGE-RESOURCES ", imageLocal?.toString())
        } catch (e: Exception) {
        }

        GlideApp.with(context)
                .load(imageLocal)
                .transition(DrawableTransitionOptions.withCrossFade(
                        GLIDE_FADE_ANIMATION_TIME_DEFAULT))
                .apply { if (isCircle) apply(RequestOptions.circleCropTransform()) }
                .error(R.drawable.ic_account_box_black_24dp)
                .into(this)
                .apply { RequestOptions().fallback(R.color.material_grey_300) }
    }

    @SuppressLint("PrivateResource")
    @BindingAdapter(value = ["app:imageLocal", "app:isCircle"], requireAll = false)
    @JvmStatic
    fun ImageView.setImageLocal(imageLocal: Drawable?, isCircle: Boolean) {
        try {
            Log.d("IMAGE-RESOURCES ", imageLocal?.toString())
        } catch (e: Exception) {
        }

        GlideApp.with(context)
                .load(imageLocal)
                .transition(DrawableTransitionOptions.withCrossFade(
                        GLIDE_FADE_ANIMATION_TIME_DEFAULT))
                .apply { if (isCircle) apply(RequestOptions.circleCropTransform()) }
                .error(R.drawable.ic_account_box_black_24dp)
                .into(this)
                .apply { RequestOptions().fallback(R.color.material_grey_300) }
    }

    @SuppressLint("PrivateResource")
    @BindingAdapter(value = ["app:imageLocal", "app:isCircle"], requireAll = false)
    @JvmStatic
    fun ImageView.setImageLocal(imageLocal: Int?, isCircle: Boolean) {
        try {
            Log.d("IMAGE-RESOURCES ", imageLocal?.toString())
        } catch (e: Exception) {
        }

        GlideApp.with(context)
                .load(imageLocal)
                .transition(DrawableTransitionOptions.withCrossFade(
                        GLIDE_FADE_ANIMATION_TIME_DEFAULT))
                .apply { if (isCircle) apply(RequestOptions.circleCropTransform()) }
                /*.error(R.drawable.abc_ic_go_search_api_material)*/
                .into(this)
                .apply { RequestOptions().fallback(R.color.material_grey_300) }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @BindingAdapter("app:webviewContent", "app:webviewTextSize")
    @JvmStatic
    fun setClearWebviewContent(webView: WebView, webviewContent: String?, webviewTextSize: Int?) {
        if (webviewContent != null) {
            webView.loadDataWithBaseURL(null, AppHelper.Func
                    .setClearWebviewContent(webviewContent), "text/html",
                    "utf-8", null)
            webView.settings.javaScriptEnabled = true
            webView.settings.defaultFontSize = if (webviewTextSize == null || webviewTextSize == 0) {
                WEBVIEW_TEXT_SIZE_DEFAULT
            } else {
                webviewTextSize
            }
        }
    }

    @BindingAdapter("app:textHtmlContent")
    @JvmStatic
    fun setHtmlTextContent(textView: TextView, text: String?) {
        if (text != null) {
            // textView.text = Jsoup.parse(text).text()
        }
    }

    @BindingAdapter("app:textAsync", "app:textSizes", requireAll = false)
    @JvmStatic
    fun setTextAsync(textView: TextView, textAsync: String?, textSizes: Int?) {
        if (textSizes != null) {
            textView.textSize = 14.toFloat()
        }
    }

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
                    setSpan(StyleSpan(BOLD),
                            noColorText.length, this.length, // this is duplicate
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE) // this is duplicate
                    setSpan(UnderlineSpan(),
                            noColorText.length, this.length, // this is duplicate
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE) // this is duplicate
                }
                text = resultSpan
            }
        }
    }

    // TODO: Build for floating label as well, this version doesn't built for floating label
    @BindingAdapter("app:customHint", "app:customFont", requireAll = true)
    @JvmStatic
    fun setEditTextHint(editText: TextInputEditText, customHint: String?, customFont: Typeface) {
        editText.apply {
            customHint?.let {
                val result = it.putSpans {
                    setSpan(CustomTypefaceSpan(customFont),
                            0,
                            this.length,
                            Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                }

                hint = result
            }
        }
    }

    @BindingAdapter("app:isPasswordType")
    @JvmStatic
    fun customTextPassword(editText: EditText, isPasswordType: Boolean) {
        if (isPasswordType)
            editText.apply {
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                typeface = DEFAULT
                transformationMethod = PasswordTransformationMethod()
            }
    }

    // TODO: Build for floating label as well, this version doesn't built for floating label
    @BindingAdapter("app:customHint", "app:customFont", requireAll = true)
    @JvmStatic
    fun setTextInputHint(textInputLayout: TextInputLayout, customHint: String?, customFont: Typeface) {
        textInputLayout.apply {
            customHint?.let {
                val result = it.putSpans {
                    setSpan(CustomTypefaceSpan(customFont),
                            0,
                            this.length,
                            Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
                }

                hint = result
            }
        }
    }

    @BindingAdapter(value = ["app:hintsShown", "app:dotsShown"], requireAll = false)
    @JvmStatic
    fun setHintWithDots(textInputLayout: TextInputLayout, hints: String?, dots: Int?) {
        textInputLayout.apply {
            hints?.let {
                hint = hints + ".".repeat(dots ?: 0)
            }
        }
    }

    // TODO: Validate password confirmation
    @BindingAdapter("app:password", "app:passwordConfirm")
    @JvmStatic
    fun isPasswordMatch(textInputLayout: TextInputLayout, password: String?, passwordConfirm: String?) {
        textInputLayout.apply {
            // TODO: Show error on textinputlayout
        }
    }

    @BindingAdapter("app:indicatorIsActive")
    @JvmStatic
    fun setIndicatorIcon(imageView: ImageView, indicatorIsActive: Boolean) {
        val size = imageView.context.resources.getDimensionPixelSize(R.dimen.dimens_12dp)
        imageView.layoutParams.height = size
        imageView.layoutParams.width = size
    }

    @JvmStatic
    @BindingAdapter(value = ["app:loadSpinnerData", "app:defaultValue"], requireAll = false)
    fun Spinner.setGenderSpinner(list: List<String?>?, defaultValue: String?) = this.let { view ->
        ArrayAdapter(
                view.context,
                R.layout.custom_spinner_item,
                list ?: listOf()
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
            view.adapter = adapter
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["app:spinnerCode", "app:viewModel"], requireAll = false)
    fun Spinner.setSpinnerClicked(spinnerCode: String?,
                                  viewModel: RxViewModel?) = this.let { view ->
        viewModel?.let {
            view.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
                }
            }
        }
    }

}