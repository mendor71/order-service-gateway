package com.mendor71.order.gateway

import com.mendor71.order.model.gateway.GatewayRequest
import com.mendor71.order.model.gateway.GatewayResponse


interface IGatewayRequestHandler<T, R> {
    suspend fun handleWithAudit(
        auditPoint: String, request: GatewayRequest<T>, handler: suspend (T) -> R
    ): GatewayResponse
}