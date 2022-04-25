package com.mendor71.order.gateway

import com.fasterxml.jackson.databind.ObjectMapper
import com.mendor71.order.gateway.utils.MdcFields
import com.mendor71.order.model.gateway.GatewayRequest
import com.mendor71.order.model.gateway.GatewayRequestType
import com.mendor71.order.model.gateway.GatewayResponse
import com.mendor71.order.model.gateway.ServiceStatus
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.slf4j.MDC
import java.util.*

class GatewayRequestHandlerTest : StringSpec({
    val auditService = DummyAuditService(objectWriter = ObjectMapper().writer())
    val handler = GatewayRequestHandler<String, String>(auditService)

    "handleWithAudit" {
        val requestId = UUID.randomUUID().toString()
        val requestType = GatewayRequestType.CREATE_ORDER

        MDC.put(MdcFields.REQUEST_ID.toString(), requestId)
        MDC.put(MdcFields.REQUEST_TYPE.toString(), requestType.toString())

        val expectedResponse = GatewayResponse(
            requestId,
            requestType,
            ServiceStatus.OK,
            "someResponse"
        )

        val response = handler.handleWithAudit(
            "TEST_AUDIT",
            GatewayRequest(requestId, requestType, "someRequest")
        ) { "someResponse" }

        response shouldBe expectedResponse
    }
})
