package com.company.map.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Customer(
    @Id
    val id: String,
    val name: String,
    val dateOfBirth: Int,
    val city: City,
    val address: String
)
