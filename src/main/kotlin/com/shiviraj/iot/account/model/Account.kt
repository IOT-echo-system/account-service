package com.shiviraj.iot.account.model

import com.shiviraj.iot.account.controller.view.AccountReqBody
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

const val ACCOUNT_COLLECTION = "accounts"

@TypeAlias("User")
@Document(ACCOUNT_COLLECTION)
data class Account(
    @Id
    var id: ObjectId? = null,
    @Indexed(unique = true)
    val accountId: AccountId,
    val name: String,
    val owner: UserId,
    val users: List<User>,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        private val defaultRoles = listOf(1, 2, 3).map { it.toString().padStart(IdType.ROLE_ID.length, '0') }
        fun from(accountId: String, accountRequest: AccountReqBody, userId: UserId): Account {
            return Account(
                accountId = accountId,
                name = accountRequest.name,
                owner = userId,
                users = listOf(User(userId, roles = defaultRoles))
            )
        }
    }
}

data class User(val userId: UserId, val roles: List<RoleId>)

typealias UserId = String
typealias AccountId = String
