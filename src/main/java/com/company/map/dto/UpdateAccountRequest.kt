package com.company.map.dto

import com.company.map.model.Currency

data class UpdateAccountRequest(
    val customerId: String,
    val balance: Double,
    val currency: Currency
)
