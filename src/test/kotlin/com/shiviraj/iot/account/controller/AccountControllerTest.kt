package com.shiviraj.iot.account.controller

import com.shiviraj.iot.account.controller.view.AccountReqBody
import com.shiviraj.iot.account.controller.view.AccountView
import com.shiviraj.iot.account.model.Account
import com.shiviraj.iot.account.service.AccountService
import com.shiviraj.iot.account.testUtils.assertNextWith
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import java.time.LocalDateTime

class AccountControllerTest {
    private val accountService = mockk<AccountService>()
    private val accountController = AccountController(accountService = accountService)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should create new account`() {
        val mockTime = LocalDateTime.of(2024, 1, 1, 1, 1)

        every { accountService.create(any(), any()) } returns Mono.just(
            Account(
                accountId = "accountId",
                name = "IOT based home automation",
                owner = "userId",
                users = emptyList(),
                createdAt = mockTime
            )
        )

        val response = accountController.createAccount(
            accountRequest = AccountReqBody(name = "IOT based home automation"),
            userId = "userId"
        )

        assertNextWith(response) {
            it shouldBe AccountView(accountId = "accountId", name = "IOT based home automation", createdAt = mockTime)
            verify(exactly = 1) {
                accountService.create(
                    accountRequest = AccountReqBody(name = "IOT based home automation"),
                    userId = "userId"
                )
            }
        }
    }
}
