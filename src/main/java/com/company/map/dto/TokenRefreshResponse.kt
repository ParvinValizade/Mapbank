package com.company.map.dto

data class TokenRefreshResponse @JvmOverloads constructor(
    val accessToken: String,
    val refreshToken: String
)
