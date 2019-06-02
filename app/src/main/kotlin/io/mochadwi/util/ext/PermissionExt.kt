package io.mochadwi.util.ext

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import io.mochadwi.util.helper.AppHelper.Const.REQUEST_LOCATION

/**
 * Created by mochadwi on 12/27/2018.
 */

// TODO: Refactor this for better permission request using kotlin idiom
fun Activity.requestPermission(requestCode: Int, permission: String): Boolean {
    return if (isPermissionDenied(permission) && isMarshmallow) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        false
    } else {
        true
    }
}

val isMarshmallow: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

@SuppressLint("MissingPermission")
fun Activity.isPermissionDenied(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED

fun Activity.setPermission(listener: PermissionListener,
                           isSameThread: Boolean = true,
                           strPermission: String) {
    Dexter.withActivity(this)
            .withPermission(strPermission)
            .withListener(listener)
            .apply { if (isSameThread) onSameThread() }
            .check()
}

fun Activity.setMultiplePermissions(multiListener: MultiplePermissionsListener,
                                    isSameThread: Boolean,
                                    vararg strPermission: String) {
    Dexter.withActivity(this)
            .withPermissions(*strPermission)
            .withListener(multiListener)
            .apply { if (isSameThread) onSameThread() }
            .check()
}

fun Activity.requestPermissionLocation(): Boolean =
        requestPermission(REQUEST_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION) ||
                requestPermission(REQUEST_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

fun Activity.checkLocationPermission(todo: (Float, Float) -> Unit) {
    if (isLocationEnabled.default) {
        todo(latitude, longitude)
    } else {
        requestPermissionLocation()
    }

}

fun Activity.checkLocationPermission(todo: (Map<String, String>) -> Unit) {
    if (isLocationEnabled.default) {
        todo(requestApiForecast(latitude, longitude))
    } else {
        requestPermissionLocation()
    }

}

fun Activity.grantedLocationPermission(requestCode: Int,
                                       grantResults: IntArray,
                                       todo: (Float, Float) -> Unit) {
    if (requestCode == REQUEST_LOCATION) {
        if (grantResults.isNotEmpty() && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {

            toastSpammable("Success")

            getLocation { lat, long ->
                todo(lat, long)
            }

            isLocationEnabled = true
        } else {
            toastSpammable("Permission Denied to access your Location")
        }
    }
}

fun Activity.grantedLocationPermission(requestCode: Int,
                                       grantResults: IntArray,
                                       todo: (Map<String, String>) -> Unit) {
    if (requestCode == REQUEST_LOCATION) {
        if (grantResults.isNotEmpty() && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {

            toastSpammable("Success")

            getLocation { lat, long ->
                latitude = lat
                longitude = long
                todo(requestApiForecast(latitude, longitude))
            }


            isLocationEnabled = true
        } else {
            toastSpammable("Permission Denied to access your Location")
        }
    }
}