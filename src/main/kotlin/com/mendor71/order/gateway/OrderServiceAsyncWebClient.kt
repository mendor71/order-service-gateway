package com.mendor71.order.gateway

import com.mendor71.order.gateway.configuration.rest.OrderServiceUris
import com.mendor71.order.model.gateway.order.CreateOrderResponse
import com.mendor71.order.model.gateway.order.GetOrderResponse
import com.mendor71.order.model.transfer.TransferOrder
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@Component
class OrderServiceAsyncWebClient(
    private val orderServiceWebClient: WebClient,
    private val uris: OrderServiceUris
) {

    suspend fun createOrder(order: TransferOrder): CreateOrderResponse =
        CreateOrderResponse(orderServiceWebClient
            .post()
            .uri(uris.create)
            .body(BodyInserters.fromValue(order))
            .retrieve()
            .bodyToMono(Long::class.java)
            .awaitSingle()
        )

    suspend fun getOrder(ordId: Long): GetOrderResponse =
        GetOrderResponse(orderServiceWebClient
            .get()
            .uri(uris.get, ordId)
            .retrieve()
            .bodyToMono(TransferOrder::class.java)
            .awaitSingle()
        )
}