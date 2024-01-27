package com.shiviraj.iot.account.exception

import com.shiviraj.iot.utils.exceptions.ServiceError


enum class IOTError(override val errorCode: String, override val message: String) : ServiceError {
    IOT0401("IOT-0401", "User already registered with this email."),
}
