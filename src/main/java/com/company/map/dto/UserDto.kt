package com.company.map.dto

import com.company.map.model.Role

data class UserDto @JvmOverloads constructor(
    val username:String,
    val password: String,
    val role: Role
)
