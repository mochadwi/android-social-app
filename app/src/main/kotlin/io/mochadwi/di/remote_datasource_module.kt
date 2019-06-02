package io.mochadwi.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import io.mochadwi.BuildConfig
import io.mochadwi.BuildConfig.BASE_URL
import io.mochadwi.data.datasource.webservice.AppWebDatasource
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Remote Web Service datasource
 */
val remoteDatasourceModule = module {
    // provided web components
    single { ChuckInterceptor(androidApplication()) }
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    single { createOkHttpClient(get<ChuckInterceptor>(), get<HttpLoggingInterceptor>()) }

    // Fill property
    single { createWebService<AppWebDatasource>(get(), BASE_URL) }
}

fun createOkHttpClient(vararg interceptors: Interceptor): OkHttpClient {
    return OkHttpClient.Builder()
            .connectTimeout(60L, TimeUnit.SECONDS)
            .readTimeout(60L, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    for (intercept in interceptors) {
                        addInterceptor(intercept)
                    }
                }
            }
            .build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    return retrofit.create(T::class.java)
}