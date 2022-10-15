package com.company.map.dto

data class TokenResponseDto @JvmOverloads constructor(
    val accessToken: String,
    val userDetails: UserDto
)
