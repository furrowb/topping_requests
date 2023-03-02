package com.bfurrow.toppings.model.http

data class CreateToppingRequest(
    val email: String,
    val toppings: List<String>,
    val location: String?
)
