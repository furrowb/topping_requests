package com.bfurrow.toppings.service

import com.bfurrow.toppings.repository.ToppingRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ToppingService(@Autowired private val toppingRepo: ToppingRepository) {
}