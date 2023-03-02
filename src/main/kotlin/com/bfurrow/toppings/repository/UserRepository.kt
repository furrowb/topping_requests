package com.bfurrow.toppings.repository

import com.bfurrow.toppings.model.User
import com.bfurrow.toppings.tables.Users.Companion.USERS
import com.bfurrow.toppings.tables.records.UsersRecord
import org.jooq.impl.DefaultDSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.ZoneOffset

@Repository
class UserRepository(@Autowired private val dslContext: DefaultDSLContext) {

    @Transactional
    fun createUser(email: String, location: String?): User {
        return dslContext.insertInto(USERS)
            .set(USERS.EMAIL, email.lowercase())
            .apply {
                if (location != null) {
                    this.set(USERS.LOCATION, location.lowercase())
                }
            }
            .set(USERS.CREATED_DATE, LocalDate.now(ZoneOffset.UTC))
            .returning()
            .fetchOne()
            ?.toUser() ?: throw Exception("Unable to insert user")
    }

    @Transactional
    fun updateUserEmail(id: Int, email: String): User? {
        val updateCount = dslContext.update(USERS)
            .set(USERS.EMAIL, email.lowercase())
            .where(USERS.ID.eq(id))
            .execute()
        if (updateCount == 0) {
            return null
        }
        return dslContext.selectFrom(USERS)
            .where(USERS.ID.eq(id))
            .fetchOne()
            ?.toUser()
    }

    fun getAllUsers(): List<User> {
        return dslContext.selectFrom(USERS)
            .fetch()
            .map {
                it.toUser()
            }
    }

    fun getUserById(id: Int): User? {
        return dslContext.selectFrom(USERS)
            .where(USERS.ID.eq(id))
            .fetchOne()
            ?.toUser()
    }

    fun getUserByEmail(email: String): User? {
        val userRecord = dslContext.selectFrom(USERS)
            .where(USERS.EMAIL.endsWithIgnoreCase(email))
            .fetchOne() ?: return null
        return userRecord.toUser()
    }

    private fun UsersRecord.toUser(): User = User(this.id!!, this.email!!, this.location)
}