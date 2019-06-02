package io.mochadwi

import android.app.Application
import com.facebook.stetho.Stetho
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.WeathericonsModule
import io.mochadwi.di.onlineSocialApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Main Application
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // start Koin context
        startKoin {
            androidContext(this@MainApplication)
            modules(onlineSocialApp)
        }

        Iconify
                .with(WeathericonsModule())

        Stetho.initializeWithDefaults(this)
    }
}
