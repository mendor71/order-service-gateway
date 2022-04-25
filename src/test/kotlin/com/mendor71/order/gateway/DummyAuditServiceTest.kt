package com.mendor71.order.gateway

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class DummyAuditServiceTest : StringSpec({
    val auditService = DummyAuditService(ObjectMapper().writer())

    "logMessage" {
        auditService.logMessage("AUDIT_POINT", "message")
    }
})
