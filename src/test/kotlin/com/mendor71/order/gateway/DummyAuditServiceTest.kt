package com.mendor71.order.gateway

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DummyAuditServiceTest : StringSpec({
    val auditService = DummyAuditService(ObjectMapper().writer())

    "logMessage" {
        auditService.logRequest("AUDIT_POINT", "message")
        auditService.logResponse("AUDIT_POINT", "message")
    }
})
