package com.mendor71.order.gateway

import com.mendor71.order.gateway.utils.ApplicationDate
import com.mendor71.order.model.gateway.GatewayRequest
import com.mendor71.order.model.gateway.GatewayResponse
import com.mendor71.order.model.gateway.ServiceStatus
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component

@Component
class GatewayRequestHandler<T, R : Any>(
    applicationDate: ApplicationDate,
    private val auditService: IAuditService,
) : IGatewayRequestHandler<T, R>(applicationDate) {

    override suspend fun handleWithAudit(
        auditPoint: String,
        request: GatewayRequest<T>,
        handler: suspend (T) -> R
    ): GatewayResponse = coroutineScope {
        auditService.logMessage("$auditPoint.REQUEST", request)

        val response = withContext(MDCContext() + this.coroutineContext) {
            try {
                okGatewayResponse(handler(request.body))
            } catch (e: NoSuchElementException) {
                notFoundGatewayResponse(e)
            } catch (e: Exception) {
                errorGatewayResponse(e)
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