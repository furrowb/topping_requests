package com.bfurrow.toppings.service

import com.bfurrow.toppings.model.User
import com.bfurrow.toppings.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(@Autowired private val userRepository: UserRepository) {
    fun getUser(id: Int): User? {
        return userRepository.getUserById(id)
    }

    fun getAllUsers(): List<User> {
        return userRepository.getAllUsers()
    }

    fun updateUserEmail(id: Int, email: String): User? {
        return userRepository.updateUserEmail(id, email)
    }
}