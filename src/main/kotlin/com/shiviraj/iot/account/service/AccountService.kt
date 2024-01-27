package com.shiviraj.iot.account.service

import com.shiviraj.iot.account.controller.view.AccountReqBody
import com.shiviraj.iot.account.model.Account
import com.shiviraj.iot.account.model.IdType
import com.shiviraj.iot.account.model.UserId
import com.shiviraj.iot.account.repository.AccountRepository
import com.shiviraj.iot.loggingstarter.logOnError
import com.shiviraj.iot.loggingstarter.logOnSuccess
import com.shiviraj.iot.mqtt.model.AuditEvent
import com.shiviraj.iot.mqtt.service.MqttPublisher
import com.shiviraj.iot.utils.audit.auditOnError
import com.shiviraj.iot.utils.audit.auditOnSuccess
import com.shiviraj.iot.utils.service.IdGeneratorService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val idGeneratorService: IdGeneratorService,
    private val mqttPublisher: MqttPublisher
) {
    fun create(accountRequest: AccountReqBody, userId: UserId): Mono<Account> {
        return idGeneratorService.generateId(IdType.ACCOUNT_ID)
            .flatMap { accountId ->
                val account = Account.from(accountId, accountRequest, userId)
                accountRepository.save(account)
                    .auditOnSuccess(
                        mqttPublisher = mqttPublisher,
                        event = AuditEvent.CREATE_ACCOUNT,
                        userId = userId,
                        accountId = accountId
                    )
                    .auditOnError(
                        mqttPublisher = mqttPublisher,
                        event = AuditEvent.CREATE_ACCOUNT,
                        userId = userId,
                        accountId = accountId
                    )
                    .logOnSuccess("Successfully created new account")
                    .logOnError(errorMessage = "Failed to create new account")
            }
    }

}
