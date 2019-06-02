package io.mochadwi.util.ext

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.esafirm.imagepicker.features.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import io.mochadwi.BuildConfig
import io.mochadwi.R
import io.mochadwi.util.helper.AppHelper
import io.mochadwi.util.helper.FileUtils.getFileFromUrlChannel
import io.mochadwi.util.helper.NavigationBundleGlobal
import io.mochadwi.util.helper.NavigationParamGlobal
import io.mochadwi.util.mvvm.SingleLiveEvent
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Created by mochadwi on 26/01/18.
 */
fun View.showSnackbarWithCustomColor(view: android.view.View, message: String,
                                     textColor: Int, backgroundColor: Int,
                                     duration: Int) {
    val finalSocial = if (TextUtils.isEmpty(message)) {
        AppHelper.Const.SERVER_ERROR_MESSAGE_DEFAULT
    } else {
        message
    }

    val finalDuration = if (duration == 0) {
        AppHelper.Const.SNACKBAR_TIMER_SHOWING_DEFAULT
    } else {
        duration
    }

    val finalTextColor = if (textColor == 0) {
        ContextCompat.getColor(view.context, R.color.mainWhiteDark)
    } else {
        textColor
    }

    val finalBackgroundColor = if (textColor == 0) {
        ContextCompat.getColor(view.context, R.color.greyBackgroundDefault)
    } else {
        backgroundColor
    }

    val snackView = Snackbar.make(view, finalSocial, finalDuration)
    snackView.setActionTextColor(finalTextColor)
    snackView.view.setBackgroundColor(finalBackgroundColor)
    snackView.show()
}

fun View.showSnackbarDefault(view: android.view.View, message: String, duration: Int = Snackbar.LENGTH_LONG) {
    val finalSocial = if (TextUtils.isEmpty(message)) {
        AppHelper.Const.SERVER_ERROR_MESSAGE_DEFAULT
    } else {
        message
    }

    val finalDuration = if (duration == 0) {
        AppHelper.Const.SNACKBAR_TIMER_SHOWING_DEFAULT
    } else {
        duration
    }

    Snackbar.make(view, finalSocial, finalDuration).show()
}

fun View.setCustomFont(view: android.view.View, fontName: String): Typeface = Typeface
        .createFromAsset(view.context.assets, "fonts/$fontName")

fun View.setupSnackbar(lifecycleOwner: LifecycleOwner,
                       snackbarSocialLiveEvent: SingleLiveEvent<String>, timeLength: Int) {
    snackbarSocialLiveEvent.observe(lifecycleOwner, Observer {
        it?.let { showSnackbarDefault(this, it, timeLength) }
    })
}

fun AppCompatActivity.showToast(context: Context, message: String) {
    Toast.makeText(context, if (TextUtils.isEmpty(message))
        AppHelper.Const.SERVER_ERROR_MESSAGE_DEFAULT else message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.startNewActivity(context: Context, navigationParamGlobal: NavigationParamGlobal) {
    startActivity(Intent(context, navigationParamGlobal.destinationPage::class.java)
            .putExtra(navigationParamGlobal.key, navigationParamGlobal.param))
}

fun AppCompatActivity.startNewActivity(context: Context, navigationBundleGlobal: NavigationBundleGlobal) {
    startActivity(Intent(context, navigationBundleGlobal.destinationPage::class.java)
            .putExtra(navigationBundleGlobal.key, navigationBundleGlobal.param)
    )
}

fun AppCompatActivity.transparentStatusBar(decorView: View) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}

fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

/**
 * create RequestBody instance from String
 */
fun String?.createPartFromString(): RequestBody = RequestBody.create(/* MediaType.parse("text/plain"),*/
        okhttp3.MultipartBody.FORM, this ?: ""
)

/**
 * create RequestBody instance from String
 */
fun File?.createPartFromFile(): RequestBody = RequestBody.create(/* MediaType.parse("text/plain"),*/
        okhttp3.MultipartBody.FORM, this
        ?: File("/storage/emulated/0/Pictures/Camera/IMG_20181231_1336242053235873.jpg")
)

fun File.prepareFilePart(partName: String): MultipartBody.Part {
    // create RequestBody instance from file
    val requestFile = RequestBody.create(MediaType.parse("image/*"), this)

    // MultipartBody.Part is used to send also the actual this name
    return MultipartBody.Part.createFormData(partName, this.name, requestFile)
}

//fun File.createRequestBody() = RequestBody.create(okhttp3.MultipartBody.FORM, this)
// TODO: Change MediaType.parse to get the actual Uri.getType()
fun File.createRequestBody(): RequestBody = RequestBody.create(MediaType.parse("image/*"), this)

inline fun <FRAGMENT : Fragment> FRAGMENT.putArgs(argsBuilder: Bundle.() -> Unit):
        FRAGMENT = this.apply { arguments = Bundle().apply(argsBuilder) }

//inline fun <FRAGMENT : Fragment> FRAGMENT.putArgss(vararg argsBuilder: (BundlePair) -> Unit):
//        FRAGMENT = apply { arguments = bundleOf(argsBuilder) }

fun File.toUri(): Uri = Uri.fromFile(this)

fun String.toUri(): Uri = Uri.fromFile(this.toFile())

fun String.toFile(): File = File(this)

fun FragmentActivity.setPermission(listener: PermissionListener,
                                   isSameThread: Boolean = true,
                                   strPermission: String) {
    Dexter.withActivity(this)
            .withPermission(strPermission)
            .withListener(listener)
            .apply { if (isSameThread) onSameThread() }
            .check()
}

fun FragmentActivity.setMultiplePermissions(multiListener: MultiplePermissionsListener,
                                            isSameThread: Boolean,
                                            vararg strPermission: String) {
    Dexter.withActivity(this)
            .withPermissions(*strPermission)
            .withListener(multiListener)
            .apply { if (isSameThread) onSameThread() }
            .check()
}

fun FragmentActivity.openCamera(requestCode: Int) {
    ImagePicker
            .cameraOnly()
            .start(this, requestCode)
}

fun Fragment.openCamera(requestCode: Int) {
    ImagePicker
            .cameraOnly()
            .start(this, requestCode)
}

suspend fun Fragment.downloadFile(url: String): Uri? = getFileFromUrlChannel(
        safeRequireContext(),
        "${BuildConfig.BASE_IMAGE_URL}$url"
).receive()

// kotlin
// (first version) as this string compared with secondVer
fun String.compareVersion(secondVer: String): Int = with(this) {
    val first: MutableList<String> = split(".") as MutableList<String>
    val second: MutableList<String> = secondVer.split(".") as MutableList<String>

    // handle different size of version
    if (first.size < second.size) first.add("0")
    else second.add("0")

    var i = 0
    while (i < first.size && i < second.size && first[i] == second[i]) {
        i++
    }

    if (i < first.size && i < second.size) {
        val diff = Integer.valueOf(first[i]).compareTo(Integer.valueOf(second[i]))
        Integer.signum(diff) // "with" keyword will directly detect this as return value
    } else {
        Integer.signum(first.size - second.size) // same with above
    }
}

// kotlin
fun Int.swap(second: Int): Unit {
    var a = this
    var b = second

    a = b.also { b = a }

    println(a)
    println(b)
}

// kotlin
// print x
fun printX(n: Int): Unit {
    if (n > 2) {
        for (i in 0 until n) {
            val j = n - 1 - i
            for (k in 0 until n) {
                if (k == i || k == j) {
                    print("*")
                } else {
                    print(" ")
                }
            }
            println()
        }
    } else {
        println("error")
    }
}

// TODO: LOGIC TEST - line 407
// ===== START ===== TASK: 1
// TASK: 1
fun twoSum(arr: List<Int>, X: Int): Boolean {
    var sumsFound = 0
    val hashTable = mutableMapOf<String, Int>()

    arr.forEachIndexed { _, elem ->

        var sumMinusElem = X - elem // equal X - arr[i]

        if (hashTable[sumMinusElem.toString()] != null) {
            sumsFound++
        }

        hashTable[elem.toString()] = elem
    }

    return sumsFound > 0 // true if more than 0
}
// ===== END ===== TASK: 1


// ===== START ===== TASK: 2
// waktunya ga cukup dan belum dalamin divide & conquer sama dynamic programming XD
// ===== END ===== TASK: 2

// ===== START ===== TASK: 3
fun Float.abs(): Float = if (this < 0.0f) this * -1 else this

fun Int.abs(): Int = if (this < 0) this * -1 else this
fun Long.abs(): Long = if (this < 0L) this * -1 else this
fun Double.abs(): Double = if (this < 0) this * -1 else this

fun Int.divide(divisor: Int): Int {
    var dividend = this
    var divisor = divisor

    // xor, if either negative mark as negative, otherwise positive
    val isNegative = if ((dividend < 0) xor (divisor < 0)) -1 else 1

    // remove negative
    dividend = dividend.abs()
    divisor = divisor.abs()

    var result = 0

    // decrement dividend until less than divisor
    while (dividend >= divisor) {
        dividend -= divisor
        ++result // keep track the result,
    }

    return isNegative * result // multiply by previous sign (negative or positive)
}
// ===== END ===== TASK: 3


// ===== START ===== TASK: 4
fun minimumCoins(denomination: List<Int>, amount: Int) {
    val denom = denomination.sortedDescending()// sort descending, array helper not 3rd-party
    var amt = amount
    val table = mutableListOf<Int>()
    for (i in 0 until denom.size)
        while (amt >= denom[i]) { // reject if coins greater than amount
            amt -= denom[i]
            table.add(denom[i]) // to store coins required
        }
    println("Coins: ${table.joinToString()}") // array helper in kotlin collection
    println("Minimum coins: ${table.size}")
}
// ===== END ===== TASK: 4