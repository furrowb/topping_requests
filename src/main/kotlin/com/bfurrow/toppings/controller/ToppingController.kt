package com.bfurrow.toppings.controller

import com.bfurrow.toppings.model.RequestedTopping
import com.bfurrow.toppings.model.RequestedToppingCount
import com.bfurrow.toppings.model.UserToppings
import com.bfurrow.toppings.model.http.CreateToppingRequest
import com.bfurrow.toppings.service.UserToppingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/toppings")
class ToppingController(@Autowired private val userToppingService: UserToppingService) {

    @PostMapping("")
    fun createTopping(@RequestBody request: CreateToppingRequest): ResponseEntity<UserToppings> {
        return ResponseEntity.status(HttpStatus.OK).body(userToppingService.createUserToppings(request.email, request.toppings))
    }

    @GetMapping("/popularity")
    fun getToppingPopularity(): ResponseEntity<List<RequestedToppingCount>> {
        return ResponseEntity.status(HttpStatus.OK).body(userToppingService.getPopularToppings())
    }

    @GetMapping("/users/{id}")
    fun getUserToppings(@PathVariable("id") userId: Int): ResponseEntity<List<RequestedTopping>> {
        return ResponseEntity.status(HttpStatus.OK).body(userToppingService.getUserToppings(userId))
    }

    /**
     * Gets the best (my personal) pizza topping preferences
     */
    @GetMapping("/best")
    fun getBestToppings(): ResponseEntity<List<RequestedTopping>> {
        return ResponseEntity.status(HttpStatus.OK).body(userToppingService.getUserToppings(1))
    }
}