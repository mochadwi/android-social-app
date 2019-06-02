package io.mochadwi

import io.mochadwi.di.testOfflineGithubApp
import io.mochadwi.di.testOnlineGithubApp
import org.junit.After
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

/**
 * Test Koin modules
 * Don't call android context here, it will not possible except you use it on the UI test
 * Ref: https://android.jlelse.eu/koin-simple-android-di-a47827a707ce
 */
class DryRunTest : KoinTest {

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun `test LocalConfiguration`() {
        startKoin {
            modules(testOfflineGithubApp)
        }.checkModules()
    }

    @Test
    fun `test RemoteConfiguration`() {
        startKoin {
            modules(testOnlineGithubApp)
        }.checkModules()
    }
}