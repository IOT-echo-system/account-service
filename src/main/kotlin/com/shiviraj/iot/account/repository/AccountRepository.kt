package com.shiviraj.iot.account.repository

import com.shiviraj.iot.account.model.Account
import com.shiviraj.iot.account.model.AccountId
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : ReactiveCrudRepository<Account, AccountId>
