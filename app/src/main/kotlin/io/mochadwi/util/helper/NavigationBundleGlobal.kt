package io.mochadwi.util.helper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * NavigationParamGlobal is a data class that using for router helper.
 *
 * @param destinationPage => Destination Page (AppCompatActivity, ex => MainActivity())
 * @param param => Param Bundle for data bucket. Using any type and convert it to Bundle
 */
data class NavigationBundleGlobal(val destinationPage: AppCompatActivity,
                                  var key: String,
                                  var param: Bundle)