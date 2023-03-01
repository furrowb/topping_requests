package com.bfurrow.toppings.controller

import com.bfurrow.toppings.exception.ResourceNotFound
import com.bfurrow.toppings.model.User
import com.bfurrow.toppings.model.http.UpdateUserRequest
import com.bfurrow.toppings.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/users")
class UserController(@Autowired private val userService: UserService) {
    @GetMapping("")
    fun getAllUser(): ResponseEntity<List<User>> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers())
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable("id") id: Int): ResponseEntity<User> {
        val user = userService.getUser(id) ?: throw ResourceNotFound()
        return ResponseEntity.status(HttpStatus.OK).body(user)
    }

    @PatchMapping("/{id}")
    fun updateUser(@PathVariable("id") id: Int, @RequestBody updateEmail: UpdateUserRequest): ResponseEntity<User> {
        val user = userService.updateUserEmail(id, updateEmail.email) ?: throw ResourceNotFound()
        return ResponseEntity.status(HttpStatus.OK).body(user)
    }
}