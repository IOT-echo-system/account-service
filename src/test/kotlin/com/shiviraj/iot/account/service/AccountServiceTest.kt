package com.shiviraj.iot.account.service

import com.shiviraj.iot.account.controller.view.AccountReqBody
import com.shiviraj.iot.account.model.Account
import com.shiviraj.iot.account.model.IdType
import com.shiviraj.iot.account.model.User
import com.shiviraj.iot.account.repository.AccountRepository
import com.shiviraj.iot.account.testUtils.assertErrorWith
import com.shiviraj.iot.account.testUtils.assertNextWith
import com.shiviraj.iot.mqtt.model.AuditEvent
import com.shiviraj.iot.mqtt.model.AuditMessage
import com.shiviraj.iot.mqtt.model.AuditStatus
import com.shiviraj.iot.mqtt.model.MqttTopicName
import com.shiviraj.iot.mqtt.service.MqttPublisher
import com.shiviraj.iot.utils.service.IdGeneratorService
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.ZoneId

class AccountServiceTest {
    private val accountRepository = mockk<AccountRepository>()
    private val idGeneratorService = mockk<IdGeneratorService>()
    private val mqttPublisher = mockk<MqttPublisher>()
    private val accountService = AccountService(
        accountRepository = accountRepository,
        idGeneratorService = idGeneratorService,
        mqttPublisher = mqttPublisher
    )
    private val mockTime = LocalDateTime.of(2024, 1, 1, 1, 1)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        mockkStatic(LocalDateTime::class)
        every { mqttPublisher.publish(any(), any()) } returns Unit
        every { LocalDateTime.now(ZoneId.of("UTC")) } returns mockTime
        every { LocalDateTime.now() } returns mockTime
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should create new account`() {
        val account = Account(
            accountId = "accountId",
            name = "IOT base home automation",
            owner = "userId",
            users = listOf(User(userId = "userId", roles = listOf("0000000001", "0000000002", "0000000003"))),
            createdAt = mockTime
        )

        every { idGeneratorService.generateId(any()) } returns Mono.just("accountId")
        every { accountRepository.save(any()) } returns Mono.just(account)

        val response = accountService.create(AccountReqBody(name = "IOT base home automation"), "userId")

        assertNextWith(response) {
            it shouldBe account

            verify(exactly = 1) {
                idGeneratorService.generateId(IdType.ACCOUNT_ID)
                accountRepository.save(account)
                mqttPublisher.publish(
                    MqttTopicName.AUDIT,
                    AuditMessage(
                        status = AuditStatus.SUCCESS,
                        userId = "userId",
                        metadata = mapOf(),
                        event = AuditEvent.CREATE_ACCOUNT,
                        accountId = "accountId",
                        deviceId = "missing-device-id",
                        timestamp = mockTime
                    )
                )
            }
        }

    }

    @Test
    fun `should failed to create new account`() {
        val account = Account(
            accountId = "accountId",
            name = "IOT base home automation",
            owner = "userId",
            users = listOf(User(userId = "userId", roles = listOf("0000000001", "0000000002", "0000000003"))),
            createdAt = mockTime
        )

        every { idGeneratorService.generateId(any()) } returns Mono.just("accountId")
        every { accountRepository.save(any()) } returns Mono.error(Exception("Failed to save in DB"))

        val response = accountService.create(AccountReqBody(name = "IOT base home automation"), "userId")

        assertErrorWith(response) {
            it shouldBe Exception("Failed to save in DB")

            verify(exactly = 1) {
                idGeneratorService.generateId(IdType.ACCOUNT_ID)
                accountRepository.save(account)
                mqttPublisher.publish(
                    MqttTopicName.AUDIT,
                    AuditMessage(
                        status = AuditStatus.FAILURE,
                        userId = "userId",
                        metadata = mapOf(),
                        event = AuditEvent.CREATE_ACCOUNT,
                        accountId = "accountId",
                        deviceId = "missing-device-id",
                        timestamp = mockTime
                    )
                )
            }
        }

    }
}
