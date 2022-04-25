package com.mendor71.order.gateway.order

import com.mendor71.order.gateway.GatewayRequestHandler
import com.mendor71.order.gateway.OrderServiceAsyncWebClient
import com.mendor71.order.model.dto.PositionDto
import com.mendor71.order.model.dto.SalePointDto
import com.mendor71.order.model.dto.StatusDto
import com.mendor71.order.model.gateway.GatewayRequestType
import com.mendor71.order.model.gateway.GatewayResponse
import com.mendor71.order.model.gateway.ServiceStatus
import com.mendor71.order.model.gateway.order.GetOrderResponse
import com.mendor71.order.model.transfer.TransferOrder
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import java.util.*

class GetOrderGatewayTest : StringSpec({

    val orderServiceClient: OrderServiceAsyncWebClient = mockk()
    val orderRequestHandler: GatewayRequestHandler<Long, GetOrderResponse> = mockk()

    val gateway = GetOrderGateway(orderServiceClient, orderRequestHandler)

    "handleRequest" {
        val response = GetOrderResponse(
            TransferOrder(
                salePoint = SalePointDto(1, 1, "salePoint"),
                status = StatusDto(1, "Order", "NEW"),
                positions = listOf(
                    PositionDto(1, 1, "iPhoneXS", 1)
                )
            )
        )
        val expectedGatewayResponse = GatewayResponse(
            UUID.randomUUID().toString(),
            GatewayRequestType.CREATE_ORDER,
            ServiceStatus.OK,
            response
        )
        coEvery { orderServiceClient.getOrder(1) } returns response
        coEvery { orderRequestHandler.handleWithAudit(any(), any(), any()) } returns expectedGatewayResponse

        val actualResponse = gateway.handleRequest(1)

        actualResponse shouldBe expectedGatewayResponse
    }
})
