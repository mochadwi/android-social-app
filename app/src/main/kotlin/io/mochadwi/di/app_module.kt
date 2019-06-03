package io.mochadwi.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import io.mochadwi.data.datasource.room.AppDatabase
import io.mochadwi.data.repository.AppRepository
import io.mochadwi.data.repository.AppRepositoryImpl
import io.mochadwi.util.rx.ApplicationSchedulerProvider
import io.mochadwi.util.rx.SchedulerProvider
import io.mochadwi.view.post.PostViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * App Components
 */
val rxModule = module {
    // Rx Schedulers
    single { ApplicationSchedulerProvider() as SchedulerProvider }
}

val viewModelModule = module {

    // ViewModel for Social app
    viewModel { PostViewModel(get(), get()) }
}

val roomModule = module {
    // Room Database
    single {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // TODO: Do real migration anything here
            }
        }
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "db_app")
//                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration() // TODO: Don't use this on production, check room json scheme for migration
                .build()
    }

    // Expose Dao directly
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().postDao() }
}

val repoModule = module {
    // App Data Repository
    single { AppRepositoryImpl(get(), get()) as AppRepository }
}

// Gather all app modules
val onlineSocialApp = listOf(rxModule, remoteDatasourceModule, roomModule, repoModule, viewModelModule)
val offlineSocialApp = listOf(rxModule, localAndroidDatasourceModule, roomModule, repoModule, viewModelModule)