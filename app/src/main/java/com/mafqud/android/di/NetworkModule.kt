package com.mafqud.android.di


import com.mafqud.android.BuildConfig
import com.mafqud.android.MyApp
import com.mafqud.android.data.RemoteDataManager
import com.mafqud.android.util.constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideApp(): MyApp {
        return MyApp.instance
    }

    @Provides
    @Singleton
    fun getOkHttpClient(myApp: MyApp, myServiceInterceptor: MyServiceInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        /**
         * @NONE : Use this log level for
         *  production environments to enhance your
         *  apps performance by skipping any logging operation.
         */
        logging.level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE

        val okHttpClient = OkHttpClient.Builder()
        val okHttpClientBuilder = okHttpClient.apply {
            retryOnConnectionFailure(true)
            readTimeout(1, TimeUnit.MINUTES)
            connectTimeout(1, TimeUnit.MINUTES)
            writeTimeout(5, TimeUnit.MINUTES)
            addInterceptor(myServiceInterceptor)
            addInterceptor(logging)
        }.build()
        return okHttpClientBuilder
    }

    @Provides
    @Singleton
    fun getMoshi(): MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }

    @Provides
    @Singleton
    fun getRetrofit(okHttpClient: OkHttpClient, moshi: MoshiConverterFactory): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            //.addConverterFactory(Json.asConverterFactory(contentType))
            .addConverterFactory(moshi)
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): RemoteDataManager {
        return retrofit.create(RemoteDataManager::class.java)
    }

}
