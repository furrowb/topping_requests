package com.bfurrow.toppings.repository

import com.bfurrow.toppings.model.RequestedTopping
import com.bfurrow.toppings.model.RequestedToppingCount
import com.bfurrow.toppings.tables.RequestedToppings.Companion.REQUESTED_TOPPINGS
import com.bfurrow.toppings.tables.records.RequestedToppingsRecord
import org.jooq.impl.DSL
import org.jooq.impl.DefaultDSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Repository
class RequestedToppingRepository(@Autowired private val dslContext: DefaultDSLContext) {

    @Transactional
    fun createTopping(userId: Int, topping: String): RequestedTopping {
        val inserts = dslContext.insertInto(REQUESTED_TOPPINGS)
            .set(REQUESTED_TOPPINGS.USER_ID, userId)
            .set(REQUESTED_TOPPINGS.TOPPING, topping)
            .set(REQUESTED_TOPPINGS.CREATED_DATE, LocalDate.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE))
            .execute()
        if (inserts == 0) {
            throw Exception("Error creating topping record for user $userId for topping $topping")
        }
        // Due to a bug with Jooq, I can't retrieve the entire record from insert/update/delete commands.
        // We can grab the record based on the input params and the query that separately.
        return dslContext.selectFrom(REQUESTED_TOPPINGS)
            .where(REQUESTED_TOPPINGS.USER_ID.eq(userId))
            .and(REQUESTED_TOPPINGS.TOPPING.eq(topping))
            .fetchOne()
            ?.let {
                it.toRequestedTopping()
            } ?: throw Exception("Unable to find created topping record for user $userId for topping $topping")
    }

    @Transactional
    fun deleteToppingsForUser(userId: Int): Boolean {
        return dslContext.deleteFrom(REQUESTED_TOPPINGS)
            .where(REQUESTED_TOPPINGS.USER_ID.eq(userId))
            .execute() > 0
    }

    fun getRankedToppings(): List<RequestedToppingCount> {
        return dslContext.select(REQUESTED_TOPPINGS.TOPPING, DSL.count())
            .from(REQUESTED_TOPPINGS)
            .groupBy(REQUESTED_TOPPINGS.TOPPING)
            .orderBy(DSL.count().desc(), REQUESTED_TOPPINGS.TOPPING)
            .fetch()
            .map {
                RequestedToppingCount(it.component1()!!, it.component2()!!)
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
        val parser: DateTimeFormatter = DateTimeFormatter.ISO_DATE
        val date = parser.parse(this.createdDate)
        return RequestedTopping(this.userId!!, this.topping!!, LocalDate.from(date))
    }
}