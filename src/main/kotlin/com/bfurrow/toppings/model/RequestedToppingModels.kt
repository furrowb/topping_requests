package com.bfurrow.toppings.model

import java.time.LocalDate

data class RequestedToppingCount(
    val toppingName: String,
    val count: Int
)

data class RequestedTopping(
    val userId: Int,
    val toppingName: String,
    val createdTime: LocalDate
)