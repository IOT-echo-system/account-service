package com.shiviraj.iot.account.controller.view

import com.shiviraj.iot.account.model.Account
import com.shiviraj.iot.account.model.AccountId
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class AccountReqBody(
    @field:NotBlank(message = "Name is required")
    val name: String
)

data class AccountView(
    val accountId: AccountId,
    val name: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(account: Account): AccountView {
            return AccountView(accountId = account.accountId, name = account.name, createdAt = account.createdAt)
        }
    }
}
