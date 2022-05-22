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
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


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

        okHttpClient.apply {
            retryOnConnectionFailure(true)
            readTimeout(1, TimeUnit.MINUTES)
            connectTimeout(1, TimeUnit.MINUTES)
            writeTimeout(5, TimeUnit.MINUTES)
            addInterceptor(myServiceInterceptor)
            addInterceptor(logging)
        }.build()
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts:  Array<TrustManager> = arrayOf(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?){}
                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate>  = arrayOf()
            })

            // Install the all-trusting trust manager
            val  sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            if (trustAllCerts.isNotEmpty() &&  trustAllCerts.first() is X509TrustManager) {
                okHttpClient.sslSocketFactory(sslSocketFactory, trustAllCerts.first() as X509TrustManager)
                okHttpClient.hostnameVerifier {  str, ssl -> true  }
            }

            return okHttpClient.build()
        } catch (e: Exception) {
            return okHttpClient.build()
        }
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
