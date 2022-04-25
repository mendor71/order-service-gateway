package com.mendor71.order.gateway

import com.fasterxml.jackson.databind.ObjectMapper
import com.mendor71.order.gateway.configuration.rest.OrderServiceUris
import com.mendor71.order.model.dto.PositionDto
import com.mendor71.order.model.dto.SalePointDto
import com.mendor71.order.model.dto.StatusDto
import com.mendor71.order.model.gateway.order.CreateOrderResponse
import com.mendor71.order.model.gateway.order.GetOrderResponse
import com.mendor71.order.model.transfer.TransferOrder
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class OrderServiceAsyncWebClientTest : StringSpec({
    val server = MockWebServer()
    val uris: OrderServiceUris = OrderServiceUris().apply {
        create = "/create"
        get = "/get"
    }
    val rootUrl = server.url("/").toString()
    val client = OrderServiceAsyncWebClient(WebClient.create(rootUrl), uris)
    val objectWriter = ObjectMapper().writer()

    afterSpec {
        server.shutdown()
    }

    "createOrder" {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("1")
        )

        val response = client.createOrder(
            TransferOrder(
                salePoint = SalePointDto(1, 1, "salePoint"),
                status = StatusDto(1, "Order", "NEW"),
                positions = listOf(
                    PositionDto(1, 1, "iPhoneXS", 1)
                )
            )
        )

        response shouldBe CreateOrderResponse(1)
    }

    "getOrder" {
        val order = TransferOrder(
            salePoint = SalePointDto(1, 1, "salePoint"),
            status = StatusDto(1, "Order", "NEW"),
            positions = listOf(
                PositionDto(1, 1, "iPhoneXS", 1)
            )
        )
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(
                    objectWriter.writeValueAsString(order)
                )
        )

        val response = client.getOrder(1)
        response shouldBe GetOrderResponse(order)
    }
})
