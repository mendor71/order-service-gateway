package com.mendor71.order.gateway.order

import com.mendor71.order.gateway.GatewayRequestHandler
import com.mendor71.order.gateway.OrderServiceAsyncWebClient
import com.mendor71.order.gateway.ServiceGateway
import com.mendor71.order.model.gateway.GatewayRequestType
import com.mendor71.order.model.gateway.GatewayResponse
import com.mendor71.order.model.gateway.order.CreateOrderResponse
import com.mendor71.order.model.transfer.TransferOrder
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Component

@Component
class CreateOrderGateway(
    private val orderServiceClient: OrderServiceAsyncWebClient,
    private val orderRequestHandler: GatewayRequestHandler<TransferOrder, CreateOrderResponse>
) : ServiceGateway<TransferOrder> {
    override val auditPoint = "CREATE_ORDER"

    override suspend fun handleRequest(request: TransferOrder): GatewayResponse = coroutineScope {
        orderRequestHandler.handleWithAudit(
            auditPoint,
            createGatewayRequest(
                GatewayRequestType.CREATE_ORDER,
                request
            ),
            orderServiceClient::createOrder
        )
    }
}