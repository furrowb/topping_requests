package com.bfurrow.toppings.controller

import com.bfurrow.toppings.service.ToppingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class ToppingController(@Autowired private val toppingService: ToppingService) {

    @PostMapping("/toppings")
    fun createTopping() {

    }

    @GetMapping("/toppings")
    fun getToppings() {

    }
}