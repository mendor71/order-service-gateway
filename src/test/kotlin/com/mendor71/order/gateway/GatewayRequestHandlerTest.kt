package com.mendor71.order.gateway

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.mendor71.order.gateway.utils.ApplicationDate
import com.mendor71.order.gateway.utils.MdcFields
import com.mendor71.order.model.gateway.GatewayRequest
import com.mendor71.order.model.gateway.GatewayRequestType
import com.mendor71.order.model.gateway.GatewayResponse
import com.mendor71.order.model.gateway.ServiceStatus
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.slf4j.MDC
import java.time.OffsetDateTime
import java.util.*

class GatewayRequestHandlerTest : StringSpec({
    val auditService = DummyAuditService(
        objectWriter = ObjectMapper()
            .registerModule(JavaTimeModule())
            .writer()
    )
    val applicationDate = mockk<ApplicationDate>()
    val handler = GatewayRequestHandler<String, String>(applicationDate, auditService)

    "handleWithAudit" {
        every { applicationDate.offsetDateTime() } returns OffsetDateTime.now()

        val requestId = UUID.randomUUID().toString()
        val requestType = GatewayRequestType.CREATE_ORDER

        MDC.put(MdcFields.REQUEST_ID.toString(), requestId)
        MDC.put(MdcFields.REQUEST_TYPE.toString(), requestType.toString())

        val responseTime = applicationDate.offsetDateTime()

        val expectedResponse = GatewayResponse(
            requestId,
            responseTime,
            requestType,
            ServiceStatus.OK,
            "someResponse"
        )

        val response = handler.handleWithAudit(
            "TEST_AUDIT",
            GatewayRequest(requestId, responseTime, requestType, "someRequest")
        ) { "someResponse" }

        response shouldBe expectedResponse
    }
})
