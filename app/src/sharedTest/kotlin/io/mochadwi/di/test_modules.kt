package io.mochadwi.di

import android.content.Context
import androidx.room.Room
import io.mochadwi.BuildConfig.BASE_URL
import io.mochadwi.MainApplication
import io.mochadwi.data.datasource.room.AppDatabase
import io.mochadwi.data.datasource.webservice.AppWebDatasource
import io.mochadwi.data.datasource.webservice.local.JavaReader
import io.mochadwi.data.datasource.webservice.local.JsonReader
import io.mochadwi.data.datasource.webservice.local.LocalFileDataSource
import io.mochadwi.util.TestSchedulerProvider
import io.mochadwi.util.mock
import io.mochadwi.util.rx.SchedulerProvider
import io.mochadwi.util.singleton
import org.koin.dsl.bind
import org.koin.dsl.module


val testAndroidModule =
        mock<MainApplication>().singleton(autoStart = true, override = true) +
                mock<Context>().singleton(autoStart = true, override = true)

/**
 * Local java json repository
 */
val testLocalJavaDatasourceModule = module {
    single { JavaReader() } bind JsonReader::class
    single { LocalFileDataSource(get(), false) as AppWebDatasource }
}

/**
 * Remote Web Service datasource
 */
val testRemoteDatasourceModule = module {
    single { createOkHttpClient() }

    // Fill property
    single { createWebService<AppWebDatasource>(get(), BASE_URL) }
}

/**
 * In-Memory Room Database definition for test purpose
 */
val testRoomModule = module {
    single {
        Room.inMemoryDatabaseBuilder(get(), AppDatabase::class.java)
                .allowMainThreadQueries() // for test purpose
                .fallbackToDestructiveMigration()
                .build()
    }

    // Expose Dao directly
    single { get<AppDatabase>().categoryDao() }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().postDao() }
}

/**
 * Test Rx
 */
val testRxModule = module {
    // provided components
    single { TestSchedulerProvider() as SchedulerProvider }
}

val testOnlineSocialApp = testAndroidModule + testRemoteDatasourceModule + testRoomModule +
        testRxModule + repoModule + viewModelModule
val testOfflineSocialApp = testAndroidModule + testLocalJavaDatasourceModule + testRoomModule +
        testRxModule + repoModule + viewModelModule
