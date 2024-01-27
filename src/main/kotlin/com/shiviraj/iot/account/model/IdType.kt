package com.shiviraj.iot.account.model

import com.shiviraj.iot.utils.service.IdSequenceType

enum class IdType(override val length: Int) : IdSequenceType {
    ACCOUNT_ID(10),
    ROLE_ID(10)
}
