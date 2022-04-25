package com.mendor71.order.gateway.api

import com.mendor71.order.gateway.order.CreateOrderGateway
import com.mendor71.order.gateway.order.GetOrderGateway
import com.mendor71.order.model.gateway.GatewayResponse
import com.mendor71.order.model.transfer.TransferOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/order-service")
class OrderServiceRestController(
    val createGateway: CreateOrderGateway,
    val getGateway: GetOrderGateway,
    val dispatcher: CoroutineDispatcher
) {

    @PostMapping("/create")
    suspend fun createOrder(@RequestBody order: TransferOrder): GatewayResponse =
        withContext(dispatcher) {
            createGateway.handleCreateRequest(order)
        }

    @PostMapping("/get/{ordId}")
    suspend fun getOrder(@PathVariable ordId: Long): GatewayResponse =
        withContext(dispatcher) {
            getGateway.handleCreateRequest(ordId)
        }
}