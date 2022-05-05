package com.mendor71.order.gateway.order

import com.mendor71.order.gateway.GatewayRequestHandler
import com.mendor71.order.gateway.OrderServiceAsyncWebClient
import com.mendor71.order.gateway.utils.ApplicationDate
import com.mendor71.order.model.dto.PositionDto
import com.mendor71.order.model.dto.SalePointDto
import com.mendor71.order.model.dto.StatusDto
import com.mendor71.order.model.gateway.GatewayRequestType
import com.mendor71.order.model.gateway.GatewayResponse
import com.mendor71.order.model.gateway.ServiceStatus
import com.mendor71.order.model.gateway.order.CreateOrderResponse
import com.mendor71.order.model.transfer.TransferOrder
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import java.util.*

class CreateOrderGatewayTest : StringSpec({
    val applicationDate = ApplicationDate()
    val orderServiceClient: OrderServiceAsyncWebClient = mockk()
    val orderRequestHandler: GatewayRequestHandler<TransferOrder, CreateOrderResponse> = mockk()

    val gateway = CreateOrderGateway(applicationDate, orderServiceClient, orderRequestHandler)

    "handleRequest" {
        val response = CreateOrderResponse(1)
        val expectedGatewayResponse = GatewayResponse(
            UUID.randomUUID().toString(),
            applicationDate.offsetDateTime(),
            GatewayRequestType.CREATE_ORDER,
            ServiceStatus.OK,
            response
        )
        coEvery { orderServiceClient.createOrder(any()) } returns response
        coEvery { orderRequestHandler.handleWithAudit(any(), any(), any()) } returns expectedGatewayResponse

        val gatewayResponse = gateway.handleRequest(
            TransferOrder(
                salePoint = SalePointDto(1, 1, "salePoint"),
                status = StatusDto(1, "Order", "NEW"),
                positions = listOf(
                    PositionDto(1, 1, "iPhoneXS", 1)
                )
            )
        )

        gatewayResponse shouldBe expectedGatewayResponse
    }
})
