package io.mochadwi.util.helper

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager

/**
 *
 * In syaa Allah created & modified
 * by mochadwi on 23/01/19
 * dedicated to build android-cus-store
 *
 */

open class AppLocationManager(private val context: Context) {
    private val getLocationManager: LocationManager
            by lazy { context.getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    fun isProviderEnabled(provider: String): Boolean = getLocationManager.isProviderEnabled(provider)

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation(provider: String): Location = getLocationManager.getLastKnownLocation(provider)
}