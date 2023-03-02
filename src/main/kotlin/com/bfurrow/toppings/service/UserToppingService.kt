package com.bfurrow.toppings.service

import com.bfurrow.toppings.model.RequestedTopping
import com.bfurrow.toppings.model.RequestedToppingCount
import com.bfurrow.toppings.model.RequestedToppingCountByLocation
import com.bfurrow.toppings.model.UserToppings
import com.bfurrow.toppings.repository.RequestedToppingRepository
import com.bfurrow.toppings.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserToppingService(@Autowired private val toppingRepo: RequestedToppingRepository, @Autowired private val userRepository: UserRepository) {

    @Transactional
    fun createUserToppings(email: String, toppings: List<String>, location: String?): UserToppings {
        val user = userRepository.getUserByEmail(email.lowercase()) ?: userRepository.createUser(email.lowercase(), location)
        toppingRepo.deleteToppingsForUser(user.id)
        val createdToppings = toppings.toSet()
            .mapNotNull { toppingRepo.createTopping(user.id, it.lowercase()) }
            .sortedBy { it.toppingName }
        return UserToppings(user, createdToppings)
    }

    fun getToppingsRank(): List<RequestedToppingCount> {
        return toppingRepo.getToppingsRank()
    }

    fun getToppingsRankByLocation(): List<RequestedToppingCountByLocation> {
        return toppingRepo.getToppingRankByLocation()
    }

    fun getUserToppings(userId: Int): List<RequestedTopping> {
        return toppingRepo.getUserToppings(userId)
    }
}