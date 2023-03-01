package com.bfurrow.toppings.model.http

data class UpdateUserRequest(
    // Optional field due to Spring not knowing how to translate a 1-field data class.
    // Also, we should already have the ID from the path variable
    val id: Int?,
    val email: String
)