package io.mochadwi.util.ext

import android.content.Context
import android.content.SharedPreferences
import io.mochadwi.util.helper.AppHelper.Const.LATITUDE_PREFERENCE
import io.mochadwi.util.helper.AppHelper.Const.LATLONG_PREFERENCE
import io.mochadwi.util.helper.AppHelper.Const.LONGITUDE_PREFERENCE
import io.mochadwi.util.helper.MyPreferencesFactory
import io.mochadwi.util.helper.MyPreferencesFactory.get
import io.mochadwi.util.helper.MyPreferencesFactory.set

/**
 * Created by mochadwi on 12/26/2018.
 */

val Context.prefs: SharedPreferences
    get() = MyPreferencesFactory.initPreferences(this)

var Context.isLocationEnabled: Boolean
    get() = prefs[LATLONG_PREFERENCE, false].default
    set(value) {
        prefs[LATLONG_PREFERENCE] = value
    }

var Context.latitude: Float
    get() = prefs[LATITUDE_PREFERENCE, 0F].default
    set(value) {
        prefs[LATITUDE_PREFERENCE] = value
    }

var Context.longitude: Float
    get() = prefs[LONGITUDE_PREFERENCE, 0F].default
    set(value) {
        prefs[LONGITUDE_PREFERENCE] = value
    }