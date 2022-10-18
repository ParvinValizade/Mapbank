package com.company.map.dto

import com.company.map.model.Role

data class CreateUserRequest @JvmOverloads constructor(
    val username:String? = null,
    val password: String? = null,
    val role: Role? = null
)
