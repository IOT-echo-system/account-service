package com.shiviraj.iot.account.controller

import com.shiviraj.iot.account.controller.view.AccountReqBody
import com.shiviraj.iot.account.controller.view.AccountView
import com.shiviraj.iot.account.model.UserId
import com.shiviraj.iot.account.service.AccountService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/accounts")
class AccountController(private val accountService: AccountService) {
    @PostMapping
    fun createAccount(
        @RequestBody @Validated accountRequest: AccountReqBody,
        @RequestHeader("UserId") userId: UserId
    ): Mono<AccountView> {
        return accountService.create(accountRequest, userId).map { AccountView.from(it) }
    }
}
