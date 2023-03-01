package com.bfurrow.toppings.service

import com.bfurrow.toppings.model.RequestedTopping
import com.bfurrow.toppings.model.RequestedToppingCount
import com.bfurrow.toppings.model.UserToppings
import com.bfurrow.toppings.repository.RequestedToppingRepository
import com.bfurrow.toppings.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserToppingService(@Autowired private val toppingRepo: RequestedToppingRepository, @Autowired private val userRepository: UserRepository) {

    fun createUserToppings(email: String, toppings: List<String>): UserToppings {
        val user = userRepository.getUserByEmail(email) ?: userRepository.createUser(email)
        toppingRepo.deleteToppingsForUser(user.id)
        val createdToppings = toppings.toSet()
            .mapNotNull { toppingRepo.createTopping(user.id, it) }
            .sortedBy { it.toppingName }
        return UserToppings(user, createdToppings)
    }

    fun getPopularToppings(): List<RequestedToppingCount> {
        return toppingRepo.getRankedToppings()
    }

    fun getUserToppings(userId: Int): List<RequestedTopping> {
        return toppingRepo.getUserToppings(userId)
    }
}