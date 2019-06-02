package io.mochadwi.util.ext

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import io.mochadwi.BuildConfig
import io.mochadwi.R
import io.mochadwi.util.helper.GlideApp
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL

/**
 * Created by mochadwi on 12/26/2018.
 */

fun ImageView.loadImage(url: String?) {
    GlideApp.with(context)
            .load(url)
            .apply(context.requestOptions(true))
            .into(this)
}

fun ImageView.loadImage(getApplicationContext: Context, file: Uri?) {
    GlideApp.with(getApplicationContext)
            .load(file)
            .apply(context.requestOptions(false))
            .into(this)
}

fun ImageView.loadImage(getApplicationContext: Context, file: Drawable?) {
    GlideApp.with(getApplicationContext)
            .load(file)
            .apply(context.requestOptions(false))
            .into(this)
}

fun ImageView.loadImage(getApplicationContext: Context, file: Bitmap?) {
    GlideApp.with(getApplicationContext)
            .asBitmap()
            .load(file)
            .apply(context.requestOptions(false))
            .into(this)
}

private fun Context.requestOptions(isOriginal: Boolean): RequestOptions =
        RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply {
                    if (isOriginal) override(getWindowWidth(), Target.SIZE_ORIGINAL)
                    else fitCenter().centerCrop()
                }
                .error(R.mipmap.ic_launcher)
                .encodeFormat(Bitmap.CompressFormat.WEBP) // compress image into webp
                .format(DecodeFormat.PREFER_RGB_565) // support decoding png or gif (image w/ alpha)
                .placeholder(myCircularProgressDrawable())
                .diskCacheStrategy(DiskCacheStrategy.ALL) // cache remote and local data directly
                .skipMemoryCache(false)

fun Context.myCircularProgressDrawable(): CircularProgressDrawable =
        CircularProgressDrawable(this).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

fun Context.getWindowWidth(): Int {
    val display = (this as Activity).windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)

    return size.x
}

fun Context.getBitmapFromUri(uri: Uri?): Bitmap =
        BitmapFactory.decodeStream(getInputStream(uri))

fun Context.getDrawableFromBitmap(bitmap: Bitmap): Drawable = BitmapDrawable(this.resources, bitmap)

fun Context.getInputStream(uri: Uri?): InputStream? =
        try {
            contentResolver.openInputStream(uri ?: getDefaultIcon)
        } catch (e: FileNotFoundException) {
            resources.openRawResource(R.raw.ic_launcher)
        }

fun String.getUriFromDrawable(scheme: String = "android.resource",
                              applicationId: String = BuildConfig.APPLICATION_ID,
                              resType: String = "drawable"): Uri =
        try {
            Uri.parse("$scheme://$applicationId/$resType/$this")
        } catch (e: NullPointerException) {
            Uri.EMPTY
        }

val getDefaultIcon: Uri? by lazy {
    try {
        "ic_launcher".getUriFromDrawable(resType = "mipmap")
    } catch (e: java.lang.Exception) {
        null
    }
}

fun getUriFromURL(urlString: String?): Uri? =
        try {
            Uri.parse(URL(urlString).toURI().toString())
        } catch (e: MalformedURLException) { // TODO: Malformed | NPE
            getDefaultIcon
        } catch (e: NullPointerException) {
            getDefaultIcon
        }

fun isImage(file: File?): Boolean = arrayOf("jpg", "png", "gif", "jpeg")
        .any { file?.name?.toLowerCase()?.endsWith(it) == true }

fun View.blinking() {
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_anim))
}

fun View.rotate() {
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_anim))
}

fun View.slideTop() {
    startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
}

fun View.stopAnim() {
    clearAnimation()
}