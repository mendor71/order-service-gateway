package com.mendor71.order.gateway.order

import com.mendor71.order.gateway.GatewayRequestHandler
import com.mendor71.order.gateway.OrderServiceAsyncWebClient
import com.mendor71.order.gateway.ServiceGateway
import com.mendor71.order.model.gateway.GatewayRequestType
import com.mendor71.order.model.gateway.GatewayResponse
import com.mendor71.order.model.gateway.order.GetOrderResponse
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Component

@Component
class GetOrderGateway(
    private val orderServiceClient: OrderServiceAsyncWebClient,
    private val orderRequestHandler: GatewayRequestHandler<Long, GetOrderResponse>
) : ServiceGateway<Long> {
    override val auditPoint: String = "GET_ORDER"

    override suspend fun handleRequest(request: Long): GatewayResponse = coroutineScope {
        orderRequestHandler.handleWithAudit(
            auditPoint,
            createGatewayRequest(
                GatewayRequestType.GET_ORDER,
                request
            ),
            orderServiceClient::getOrder
        )
    }
}