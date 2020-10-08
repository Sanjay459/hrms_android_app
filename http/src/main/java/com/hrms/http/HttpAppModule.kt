package com.hrms.http

import android.content.Context
import android.content.SharedPreferences
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import dagger.Lazy
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Named

class HttpAppModule {

    fun provideAccessTokenRepository(sharedPreferences: SharedPreferences): AccessTokenRepository {
        return AccessTokenRepository(sharedPreferences)
    }

    fun provideAuthenticator(
        accessTokenRepository: AccessTokenRepository,
        oAuthApi: Lazy<IOAuthApi>
    ): OkHttpAuthenticator {
        return OkHttpAuthenticator(RefreshAuthenticator(accessTokenRepository, oAuthApi))
    }

    fun provideHeaderInterceptor(accessTokenRepository: AccessTokenRepository): HeaderInterceptor {
        return HeaderInterceptor(accessTokenRepository)
    }

    fun provideRetrofitOkHttpClient(
        context: Context,
        authenticator: OkHttpAuthenticator,
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        val logging = HttpFiltratedLoggingInterceptor(
            setOf(
                "api/v1/profile_photo/",
                "api/v1/user_photos/"
            )
        ).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder().apply {
            authenticator(authenticator)
            addInterceptor(headerInterceptor)
            addNetworkInterceptor(StethoInterceptor())
            if (BuildConfig.DEBUG) {
                addInterceptor(logging)
            }
            cache(Cache(context.cacheDir, CACHE_SIZE))
        }.build()
    }

    fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }

    fun provideRetrofit(
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
        okHttpClient: OkHttpClient,
        adapterMap: Lazy<Map<Type, Any>>,
        rootUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                .apply {
                    setLenient()
                    adapterMap.get().forEach {
                        registerTypeAdapter(it.key, it.value)
                    }
                }
                .create()))
            .baseUrl(rootUrl)
            .build()
    }
}