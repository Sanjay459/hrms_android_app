package com.hrms.http

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val repository: AccessTokenRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = repository.getToken().accessToken

        if (accessToken.isNotEmpty()) {
            val newRequest = chain.request().newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
            return chain.proceed(newRequest)
        }

        return chain.proceed(chain.request())
    }
}
