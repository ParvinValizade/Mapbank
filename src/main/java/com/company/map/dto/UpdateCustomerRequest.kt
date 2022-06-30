package com.company.map.dto

data class UpdateCustomerRequest(
    val name: String,
    val city: CityDto,
    val address: String
)
