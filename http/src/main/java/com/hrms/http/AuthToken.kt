package com.hrms.http

data class AuthToken(val accessToken: String, val refreshToken: String) {
    fun hasTokens(): Boolean =
        accessToken.isNotEmpty() && refreshToken.isNotEmpty()
}
