package com.company.map.dto

import com.company.map.model.Role

data class UserDto @JvmOverloads constructor(
    val userId: Long,
    val username:String,
    val password: String,
    val role: Role
)
