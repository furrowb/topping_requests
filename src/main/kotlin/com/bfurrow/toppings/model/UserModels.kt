package com.bfurrow.toppings.model

data class User(
    val id: Int,
    val email: String,
    val location: String?
)

data class UserToppings(
    val user: User,
    val toppings: List<RequestedTopping>
)