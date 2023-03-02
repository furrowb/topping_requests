package com.bfurrow.toppings.repository

import com.bfurrow.toppings.model.RequestedTopping
import com.bfurrow.toppings.model.RequestedToppingCount
import com.bfurrow.toppings.model.RequestedToppingCountByLocation
import com.bfurrow.toppings.tables.RequestedToppings.Companion.REQUESTED_TOPPINGS
import com.bfurrow.toppings.tables.Users.Companion.USERS
import com.bfurrow.toppings.tables.records.RequestedToppingsRecord
import org.jooq.impl.DSL
import org.jooq.impl.DefaultDSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.ZoneOffset

@Repository
class RequestedToppingRepository(@Autowired private val dslContext: DefaultDSLContext) {

    @Transactional
    fun createTopping(userId: Int, topping: String): RequestedTopping? {
        return dslContext.insertInto(REQUESTED_TOPPINGS)
            .set(REQUESTED_TOPPINGS.USER_ID, userId)
            .set(REQUESTED_TOPPINGS.TOPPING, topping)
            .set(REQUESTED_TOPPINGS.CREATED_DATE, LocalDate.now(ZoneOffset.UTC))
            .onConflictDoNothing()
            .returning()
            .fetchOne()
            ?.toRequestedTopping()
    }

    @Transactional
    fun deleteToppingsForUser(userId: Int): Boolean {
        return dslContext.deleteFrom(REQUESTED_TOPPINGS)
            .where(REQUESTED_TOPPINGS.USER_ID.eq(userId))
            .execute() > 0
    }

    fun getToppingsRank(): List<RequestedToppingCount> {
        return dslContext.select(REQUESTED_TOPPINGS.TOPPING, DSL.count())
            .from(REQUESTED_TOPPINGS)
            .groupBy(REQUESTED_TOPPINGS.TOPPING)
            .orderBy(DSL.count().desc(), REQUESTED_TOPPINGS.TOPPING)
            .fetch()
            .map {
                RequestedToppingCount(it.component1()!!, it.component2()!!)
            }
    }

    fun getToppingRankByLocation(): List<RequestedToppingCountByLocation> {
        val locationGrouping = mutableMapOf<String, List<RequestedToppingCount>>()
        dslContext.select(REQUESTED_TOPPINGS.TOPPING, USERS.LOCATION, DSL.count())
            .from(REQUESTED_TOPPINGS)
            .innerJoin(USERS).on(USERS.ID.eq(REQUESTED_TOPPINGS.USER_ID))
            .groupBy(REQUESTED_TOPPINGS.TOPPING, USERS.LOCATION)
            .fetch()
            .forEach {
                val topping = it.component1()!!
                val location = it.component2()?.lowercase()
                val count = it.component3()
                if (location != null) {
                    locationGrouping.merge(location, listOf(RequestedToppingCount(topping, count))) { list1, list2 ->
                        listOf(list1, list2).flatten()
                    }
                } else {
                    locationGrouping.merge("unspecified", listOf(RequestedToppingCount(topping, count))) { list1, list2 ->
                        listOf(list1, list2).flatten()
                    }
                }
            }
        return locationGrouping.map { keyValue ->
            RequestedToppingCountByLocation(keyValue.key,
                keyValue.value.sortedWith(
                    compareByDescending<RequestedToppingCount> { it.count }.thenBy { it.toppingName }
                )
            )
        }
    }

    fun getUserToppings(userId: Int): List<RequestedTopping> {
        return dslContext.selectFrom(REQUESTED_TOPPINGS)
            .where(REQUESTED_TOPPINGS.USER_ID.eq(userId))
            .fetch()
            .map {
                it.toRequestedTopping()
            }
    }

    private fun RequestedToppingsRecord.toRequestedTopping(): RequestedTopping {
        return RequestedTopping(this.userId!!, this.topping!!, this.createdDate!!)
    }
}