package com.hrms.http

import android.content.SharedPreferences
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

class AccessTokenRepository(private val sharedPreferences: SharedPreferences) {

    private val tokenCleared: PublishRelay<Unit> = PublishRelay.create()

    companion object {
        private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
        private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY"
    }

    fun setToken(token: AuthToken) {
        sharedPreferences.edit()
            .putString(ACCESS_TOKEN_KEY, token.accessToken)
            .putString(REFRESH_TOKEN_KEY, token.refreshToken)
            .apply()
    }

    fun getToken(): AuthToken {
        val accessToken: String = sharedPreferences.getString(ACCESS_TOKEN_KEY, "")!!
        val refreshToken: String = sharedPreferences.getString(REFRESH_TOKEN_KEY, "")!!

        return AuthToken(accessToken, refreshToken)
    }

    fun removeToken() {
        sharedPreferences.edit()
            .remove(ACCESS_TOKEN_KEY)
            .remove(REFRESH_TOKEN_KEY)
            .apply()

        tokenCleared.accept(Unit)
    }

    fun observeTokenCleared(): Observable<Unit> {
        return tokenCleared.hide()
    }
}
