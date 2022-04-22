package com.mendor71.order.gateway.api

import com.mendor71.order.gateway.OrderServiceAsyncWebClient
import com.mendor71.order.gateway.OrderServiceGateway
import com.mendor71.order.gateway.ServiceResult
import com.mendor71.order.model.transfer.TransferOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/order-service")
class OrderServiceRestController(
    val orderServiceGateway: OrderServiceGateway,
    val dispatcher: CoroutineDispatcher
) {

    @PostMapping("/create")
    suspend fun createOrder(@RequestBody order: TransferOrder): ServiceResult = withContext(dispatcher) {
        orderServiceGateway.handleRequest(order)
    }
}