package com.shiviraj.iot.account.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.TypeAlias
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

const val ROLE_COLLECTION = "roles"

@TypeAlias("User")
@Document(ROLE_COLLECTION)
data class Role(
    @Id
    var id: ObjectId? = null,
    @Indexed(unique = true)
    val roleId: RoleId,
    val name: String,
    val accountId: AccountId,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

typealias RoleId = String
