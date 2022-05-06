package com.mendor71.order.gateway

import org.slf4j.Logger

interface IAuditService {
    val logger: Logger?

    fun logRequest(auditPoint: String, message: Any)
    fun logResponse(auditPoint: String, message: Any)
}

data class AuditMessage(val auditPoint: String, val payload: Any)