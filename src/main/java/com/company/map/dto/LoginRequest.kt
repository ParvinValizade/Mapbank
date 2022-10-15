package com.company.map.dto

data class LoginRequest @JvmOverloads constructor(
    val username: String,
    val password: String
)
