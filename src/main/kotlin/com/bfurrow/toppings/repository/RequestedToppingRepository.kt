package com.bfurrow.toppings.repository

import com.bfurrow.toppings.Tables
import org.jooq.impl.DSL
import org.jooq.impl.DefaultDSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class RequestedToppingRepository(@Autowired private val dslContext: DefaultDSLContext) {

    @Transactional
    fun createTopping(userId: Int, topping: String) {
        dslContext.insertInto(Tables.REQUESTED_TOPPINGS)
            .set(Tables.REQUESTED_TOPPINGS.USER_ID, userId)
            .set(Tables.REQUESTED_TOPPINGS.TOPPING, topping)
            .execute()
    }

    fun getRankedToppings() {
        dslContext.select(Tables.REQUESTED_TOPPINGS.TOPPING, DSL.count())
            .groupBy(Tables.REQUESTED_TOPPINGS.TOPPING)
            .orderBy(DSL.count().desc(), Tables.REQUESTED_TOPPINGS.TOPPING)
            .fetch()
            .map {

            }
    }

}