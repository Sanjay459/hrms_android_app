package com.hrms.http

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

class HttpFiltratedLoggingInterceptor(
    private val excludePaths: Set<String>
) : Interceptor {
    private val delegate = HttpLoggingInterceptor()

    var level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY
        set(value) {
            delegate.level = value
            field = value
        }
        get() = delegate.level


    override fun intercept(chain: Interceptor.Chain): Response {
        val path = chain.request().url.encodedPath.substring(1)

        return if (excludePaths.contains(path)) {
            val oldLevel = level
            level = HttpLoggingInterceptor.Level.HEADERS
            val resp = delegate.intercept(chain)
            level = oldLevel

            resp
        } else {
            delegate.intercept(chain)
        }
    }

}