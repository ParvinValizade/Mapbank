package com.company.map.dto

import com.company.map.model.Currency

data class CreateAccountRequest(
    val id: String,
    val customerId: String,
    val balance: Double,
    val currency: Currency
)
