package com.company.map.dto

data class MoneyTransferRequest(
    val fromId: String?,
    val toId: String?,
    val amount: Double?
){
    constructor():this(
        null,
        null,
        null
        )
}
