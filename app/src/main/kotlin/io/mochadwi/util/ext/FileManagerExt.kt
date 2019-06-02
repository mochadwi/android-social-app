package io.mochadwi.util.ext

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.fragment.app.Fragment
import java.io.ByteArrayOutputStream
import java.io.File


/**
 * Created by mochadwi on 12/26/2018.
 */

const val REQUEST_IMAGE = 555

fun Fragment.openGallery() {
    Intent().apply {
        action = Intent.ACTION_GET_CONTENT
        type = "image/*"

        startActivityForResult(Intent.createChooser(this, "Select Picture"), REQUEST_IMAGE)
    }
}

fun Context.getByteArrayFromUri(uri: Uri?): ByteArray {
    val bitmap = getBitmapFromUri(uri)
    val stream = ByteArrayOutputStream()

    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    bitmap.recycle()

    return stream.toByteArray()
}

// Get length of file in bytes
val File.fileSizeInBytes: Long
    get() = length()

// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
val File.fileSizeInKB: Long
    get() = fileSizeInBytes / 1024
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
val File.fileSizeInMB: Long
    get() = fileSizeInKB / 1024

// Get length of file in bytes
val ByteArray.fileSizeInBytes: Long
    get() = size.toLong()

// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
val ByteArray.fileSizeInKB: Long
    get() = fileSizeInBytes / 1024
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
val ByteArray.fileSizeInMB: Long
    get() = fileSizeInKB / 1024