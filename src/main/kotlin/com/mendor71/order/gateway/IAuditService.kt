package com.mendor71.order.gateway

import org.slf4j.Logger

interface IAuditService {
    val logger: Logger?

    fun logMessage(auditPoint: String, message: Any)
}

data class AuditMessage(val auditPoint: String, val payload: Any)