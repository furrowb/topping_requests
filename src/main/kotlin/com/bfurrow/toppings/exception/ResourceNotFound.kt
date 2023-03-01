package com.bfurrow.toppings.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Resource not found")
class ResourceNotFound: Exception()