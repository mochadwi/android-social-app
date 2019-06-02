package io.mochadwi.util.ext

import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.mochadwi.BuildConfig
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import java.io.*

/**
 * Created by mochadwi on 12/9/2018.
 */

lateinit var spotsDialog: AlertDialog

inline fun <reified T> Activity.switchToPage(args: Bundle? = null) {
    startActivity(Intent(this, T::class.java).apply { args?.let { putExtras(args) } })
}

// TODO: Build support anko intenfor on this extension
inline fun <reified T> Activity.intentForClear(args: Bundle? = null) {
    startActivity(Intent(this, T::class.java).apply {
        clearTask()
        clearTask()
        newTask()
        args?.let { putExtras(args) }
    })
}

inline fun <reified T> Activity.toPageFromBeginning() {
    startActivity(Intent(this, T::class.java))
    finishAffinity()
}

inline fun <reified T> showLogInfo(message: String) {
    if (BuildConfig.DEBUG)
        Log.i(T::class.java.simpleName + " #customlog", message)
}

inline fun <reified T> showLogDebug(message: String) {
    if (BuildConfig.DEBUG)
        Log.d(T::class.java.simpleName + " #customlog", message)
}

inline fun <reified T> showLogError(message: String) {
    if (BuildConfig.DEBUG)
        Log.e(T::class.java.simpleName + " #customlog", message)
}

lateinit var mToast: Toast

fun Context.toast(msg: String, length: Int = Toast.LENGTH_LONG) {
    mToast = Toast.makeText(this, msg, length).apply {
        show()
    }
}

fun Context.toastSpammable(msg: String) {
    if (::mToast.isInitialized) mToast.cancel()
    mToast = Toast.makeText(this, msg, Toast.LENGTH_LONG).apply {
        show()
    }
}

fun Fragment.toastSpammable(msg: String) {
    requireActivity().apply {
        if (::mToast.isInitialized) mToast.cancel()
        mToast = Toast.makeText(this, msg, Toast.LENGTH_LONG).apply {
            show()
        }
    }
}

inline fun <reified MODEL> MODEL.toJson(serializer: KSerializer<MODEL>): String =
        Json.stringify(serializer, this).replace("\\\\n", "\\n")

inline fun <reified MODEL> String.fromJson(serializer: KSerializer<MODEL>): MODEL =
        Json.parse(serializer, this)

fun hideKeyboard(getActivity: Activity?) {
    getActivity?.apply {
        (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        }
    }
}

fun Context.showTimePicker(listener: TimePickerDialog.OnTimeSetListener) {
    TimePickerDialog(this, listener, 8, 0, true).apply {
        setTitle("Select Time")
        show()
    }
}

fun Int.leadNumberWithZero(): String {
    return String.format("%02d", this)
}

fun <T : Serializable> deepCopy(obj: T?): T? {
    if (obj == null) return null
    val baos = ByteArrayOutputStream()
    val oos = ObjectOutputStream(baos)
    oos.writeObject(obj)
    oos.close()
    val bais = ByteArrayInputStream(baos.toByteArray())
    val ois = ObjectInputStream(bais)
    @Suppress("unchecked_cast")
    return ois.readObject() as T
}

fun Fragment.safeRequireActivity(): FragmentActivity? = if (isAdded) requireActivity() else activity

fun Fragment.safeRequireContext(): Context? = if (isAdded) requireContext() else context

fun String?.safeToInt(): Int = this?.toIntOrNull() ?: 0

val Boolean?.isOpen: Int
    get() = if (this != null && this) 0 else 1
