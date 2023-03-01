package com.bfurrow.toppings

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class ToppingsApplication

fun main(args: Array<String>) {
	runApplication<ToppingsApplication>(*args)
}
