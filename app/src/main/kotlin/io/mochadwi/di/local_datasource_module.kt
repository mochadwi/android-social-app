package io.mochadwi.di

import io.mochadwi.data.datasource.webservice.AppWebDatasource
import io.mochadwi.data.datasource.webservice.local.AndroidJsonReader
import io.mochadwi.data.datasource.webservice.local.JsonReader
import io.mochadwi.data.datasource.webservice.local.LocalFileDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * Local Json Files Datasource
 */
val localAndroidDatasourceModule = module {
    single { AndroidJsonReader(androidApplication()) as JsonReader }
    single { LocalFileDataSource(get(), true) as AppWebDatasource }
}