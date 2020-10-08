package com.hrms.imageloader

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

@Module
object ImageLoaderModule {
    private const val PICASSO_CACHE_DIR: String = "picasso-image-cache"
    private const val PICASSO_CACHE_SIZE: Long = 10 * 1024 * 1024

    @Provides
    @ApplicationScope
    @JvmStatic
    fun provideOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(Cache(File(context.cacheDir, PICASSO_CACHE_DIR), PICASSO_CACHE_SIZE))
            .build()
    }

    @Provides
    @ApplicationScope
    @JvmStatic
    fun providePicasso(context: Context, okHttpClient: OkHttpClient): Picasso {
        return Picasso.Builder(context)
            .downloader(OkHttp3Downloader(okHttpClient))
            .build()
    }

    @Provides
    @JvmStatic
    fun provideImageLoader(picasso: Picasso): ImageLoader {
        return ImageLoader(picasso)
    }
}
