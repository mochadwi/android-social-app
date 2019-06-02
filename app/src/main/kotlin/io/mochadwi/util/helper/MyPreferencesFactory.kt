package io.mochadwi.util.helper

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import io.mochadwi.util.helper.AppHelper.Const.MY_PREFERENCES


/**
 * Created by mochadwi on 12/9/2018.
 */
object MyPreferencesFactory {

    private inline fun SharedPreferences.edit(operation: SharedPreferences.Editor.() -> Unit) {
        edit().apply {
            operation(this)
            apply()
        }
    }

    // TODO: Use this instead to refresh prefs
    fun SharedPreferences.clearPref() {
        edit().clear().apply()
    }

    fun initPreferences(context: Context, name: String = MY_PREFERENCES): SharedPreferences =
            with(context) {
                getSharedPreferences(name, MODE_PRIVATE)
            }

    /**
     * finds value on given key.
     * [T] is the type of value
     * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    inline operator fun <reified T : Any?> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String ?: "") as? T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as? T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as? T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1F) as? T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1L) as? T?
            else -> throw UnsupportedOperationException("Pref factory get not yet implemented")
        }
    }

    /**
     * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
     */
    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { putString(key, value) }
            is Int -> edit { putInt(key, value) }
            is Boolean -> edit { putBoolean(key, value) }
            is Float -> edit { putFloat(key, value) }
            is Long -> edit { putLong(key, value) }
            else -> throw UnsupportedOperationException("Pref factory set not yet implemented")
        }
    }
}

// TODO: Build deep copy using and observe previous and replace with new data
/*lateinit var oldAccount: ResultModel
lateinit var newAccount: ResultModel
private var newAccount: ResultModel by Delegates.observable(ResultModel()) { property, oldValue, newValue ->
//            showLogDebug(this::class.java, "account: ${newAccount?.toJson()}")
//            print("$newValue")
oldAccount = oldValue
}*/
