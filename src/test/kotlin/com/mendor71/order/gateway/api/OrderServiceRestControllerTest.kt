package com.mendor71.order.gateway.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.mendor71.order.gateway.order.CreateOrderGateway
import com.mendor71.order.gateway.order.GetOrderGateway
import com.mendor71.order.gateway.testconfiguration.RestControllerTestConfiguration
import com.mendor71.order.gateway.utils.ApplicationDate
import com.mendor71.order.model.dto.PositionDto
import com.mendor71.order.model.dto.SalePointDto
import com.mendor71.order.model.dto.StatusDto
import com.mendor71.order.model.gateway.GatewayRequestType
import com.mendor71.order.model.gateway.GatewayResponse
import com.mendor71.order.model.gateway.ServiceStatus
import com.mendor71.order.model.gateway.order.CreateOrderResponse
import com.mendor71.order.model.gateway.order.GetOrderResponse
import com.mendor71.order.model.transfer.TransferOrder
import io.kotest.core.spec.style.StringSpec
import io.mockk.coEvery
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@Import(RestControllerTestConfiguration::class)
@WebFluxTest(OrderServiceRestController::class)
@AutoConfigureWebTestClient(timeout = "10000")
class OrderServiceRestControllerTest(
    private val webTestClient: WebTestClient,
    private val createGateway: CreateOrderGateway,
    private val getGateway: GetOrderGateway,
    private val applicationDate: ApplicationDate
) : StringSpec({

    val objectWriter = ObjectMapper().writer()

    val transferOrder = TransferOrder(
        salePoint = SalePointDto(1, 1, "salePoint"),
        status = StatusDto(1, "Order", "NEW"),
        positions = listOf(
            PositionDto(1, 1, "iPhoneXS", 1)
        )
    )

    "createOrder" {
        val response = GatewayResponse(
            "123",
            applicationDate.offsetDateTime(),
            GatewayRequestType.CREATE_ORDER,
            ServiceStatus.OK,
            CreateOrderResponse(1),
        )
        coEvery { createGateway.handleRequest(any()) } returns response

        webTestClient.post()
            .uri("/order-service/create")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(objectWriter.writeValueAsString(transferOrder))
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.requestId").isEqualTo("123")
            .jsonPath("$.type").isEqualTo("CREATE_ORDER")
            .jsonPath("$.status").isEqualTo("OK")
            .jsonPath("$.body.ordId").isEqualTo(1)
    }

    "getOrder" {
        val response = GatewayResponse(
            "123",
            applicationDate.offsetDateTime(),
            GatewayRequestType.GET_ORDER,
            ServiceStatus.OK,
            GetOrderResponse(transferOrder),
        )
        coEvery { getGateway.handleRequest(any()) } returns response
        webTestClient.post()
            .uri("/order-service/get/1")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.requestId").isEqualTo("123")
            .jsonPath("$.type").isEqualTo("GET_ORDER")
            .jsonPath("$.status").isEqualTo("OK")
            .jsonPath("$.body.order.salePoint.spName").isEqualTo("salePoint")
            .jsonPath("$.body.order.status.stName").isEqualTo("NEW")
    }
})
