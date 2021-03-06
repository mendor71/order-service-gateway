package com.mendor71.order.gateway

import com.mendor71.order.gateway.utils.ApplicationDate
import com.mendor71.order.gateway.utils.MdcFields
import com.mendor71.order.model.gateway.GatewayRequest
import com.mendor71.order.model.gateway.GatewayRequestType
import com.mendor71.order.model.gateway.GatewayResponse
import org.slf4j.MDC
import java.util.*

abstract class ServiceGateway<T>(
    private val applicationDate: ApplicationDate
) {
    abstract val auditPoint: String

    fun <T> createGatewayRequest(
        requestType: GatewayRequestType,
        request: T
    ): GatewayRequest<T> {
        val requestId = UUID.randomUUID().toString()
        MDC.put(MdcFields.REQUEST_ID.toString(), requestId)
        MDC.put(MdcFields.REQUEST_TYPE.toString(), requestType.toString())
        return GatewayRequest(
            requestId,
            applicationDate.offsetDateTime(),
            requestType,
            request
        )
    }

    abstract suspend fun handleRequest(request: T): GatewayResponse
}