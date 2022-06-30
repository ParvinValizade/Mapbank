package com.company.map.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Account(
    @Id
    val id: String,
    val customerId: String,
    val balance: Double,
    val city: City,
    val currency: Currency
)
