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
    fun createUser(email: String): User {
        val userWithId = dslContext.insertInto(USERS)
            .set(USERS.EMAIL, email)
            .set(USERS.CREATED_DATE, LocalDate.now(ZoneOffset.UTC))
            .returning(USERS.ID)
            .fetchOne() ?: throw Exception("Unable to insert user")
        // Due to a bug with Jooq, I can't retrieve the entire record from insert/update/delete commands, but we can retrieve a single field.
        // Since one of the fields is an ID, we can grab the ID and the query the record separately.
        val data = dslContext.selectFrom(USERS)
            .where(USERS.ID.eq(userWithId.id))
            .fetchOne() ?: throw Exception("Unable to find inserted user")
        return data.toUser()
    }

    @Transactional
    fun updateUserEmail(id: Int, email: String): User? {
        // Jooq bug required the field to be aliased to work. Otherwise, it generated incorrect SQL
        val emailAlias = USERS.EMAIL.`as`("email")
        val updatedCount = dslContext.update(USERS)
            .set(emailAlias, email)
            .where(USERS.ID.eq(id))
            .execute()
        if (updatedCount == 0) {
            return null
        }
        // Due to a bug with Jooq, I can't retrieve the entire record from insert/update/delete commands.
        // Instead, we will just have to query for the record ourselves.
        return dslContext.selectFrom(USERS)
                .where(USERS.ID.eq(id))
                .fetchOne()?.toUser()
    }

    fun getAllUsers(): List<User> {
        return dslContext.selectFrom(USERS)
            .fetch()
            .map {
                it.toUser()
            }
    }

    fun getUserById(id: Int): User? {
        val userRecord = dslContext.selectFrom(USERS)
            .where(USERS.ID.eq(id))
            .fetchOne() ?: return null
        return User(userRecord.id!!, userRecord.email!!)
    }

    fun getUserByEmail(email: String): User? {
        val userRecord = dslContext.selectFrom(USERS)
            .where(USERS.EMAIL.eq(email))
            .fetchOne() ?: return null
        return userRecord.toUser()
    }

    private fun UsersRecord.toUser(): User = User(this.id!!, this.email!!)
}