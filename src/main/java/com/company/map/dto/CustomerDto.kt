package com.company.map.dto

data class CustomerDto(
    val id: String,
    val name: String,
    val dateOfBirth: Int,
    val city: CityDto,
    val address: String
)
