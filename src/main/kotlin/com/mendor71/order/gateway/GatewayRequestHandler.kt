package com.mendor71.order.gateway

import com.mendor71.order.gateway.utils.errorGatewayResponse
import com.mendor71.order.gateway.utils.notFoundGatewayResponse
import com.mendor71.order.gateway.utils.okGatewayResponse
import com.mendor71.order.model.gateway.GatewayRequest
import com.mendor71.order.model.gateway.GatewayResponse
import com.mendor71.order.model.gateway.ServiceStatus
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component

@Component
class GatewayRequestHandler<T, R : Any>(
    private val auditService: IAuditService
) : IGatewayRequestHandler<T, R> {

    override suspend fun handleWithAudit(
        auditPoint: String,
        request: GatewayRequest<T>,
        handler: suspend (T) -> R
    ): GatewayResponse = coroutineScope {
        auditService.logMessage("$auditPoint.REQUEST", request)

        val response = withContext(MDCContext() + this.coroutineContext) {
            try {
                handler(request.body).okGatewayResponse()
            } catch (e: NoSuchElementException) {
                e.notFoundGatewayResponse()
            } catch (e: Exception) {
                e.errorGatewayResponse()
            }
        }

        auditService.logMessage(
            if (response.status == ServiceStatus.OK) {
                "$auditPoint.RESPONSE"
            } else {
                "$auditPoint.ERROR"
            }, response
        )

        response
    }
}