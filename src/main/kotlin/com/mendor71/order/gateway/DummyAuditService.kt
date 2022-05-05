package com.mendor71.order.gateway

import com.fasterxml.jackson.databind.ObjectWriter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "audit", name = ["enabled"], havingValue = "false")
class DummyAuditService(
    private val objectWriter: ObjectWriter
) : IAuditService {

    override val logger: Logger = LoggerFactory.getLogger(DummyAuditService::class.simpleName)

    override fun logMessage(auditPoint: String, message: Any) {
        logger.info(objectWriter.writeValueAsString(AuditMessage(auditPoint, message)))
    }
}