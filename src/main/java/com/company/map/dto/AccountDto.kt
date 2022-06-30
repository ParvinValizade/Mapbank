package com.company.map.dto

import com.company.map.model.Currency

data class AccountDto(
    val id: String,
    val customerId: String,
    val balance: Double,
    val currency: Currency
)
