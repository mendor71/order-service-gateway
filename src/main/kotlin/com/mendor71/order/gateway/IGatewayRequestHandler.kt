package com.mendor71.order.gateway

import com.mendor71.order.gateway.utils.ApplicationDate
import com.mendor71.order.gateway.utils.MdcFields
import com.mendor71.order.model.gateway.GatewayRequest
import com.mendor71.order.model.gateway.GatewayRequestType.Companion.gatewayRequestType
import com.mendor71.order.model.gateway.GatewayResponse
import com.mendor71.order.model.gateway.ServiceStatus
import org.slf4j.MDC

abstract class IGatewayRequestHandler<T, R>(private val applicationDate: ApplicationDate) {
    abstract suspend fun handleWithAudit(
        auditPoint: String, request: GatewayRequest<T>, handler: suspend (T) -> R
    ): GatewayResponse

    fun okGatewayResponse(r: R) = GatewayResponse(
        MDC.get(MdcFields.REQUEST_ID.toString()),
        applicationDate.offsetDateTime(),
        MDC.get(MdcFields.REQUEST_TYPE.toString()).gatewayRequestType(),
        status = ServiceStatus.OK,
        body = r
    )

    fun errorGatewayResponse(ex: Throwable) = GatewayResponse(
        MDC.get(MdcFields.REQUEST_ID.toString()),
        applicationDate.offsetDateTime(),
        MDC.get(MdcFields.REQUEST_TYPE.toString()).gatewayRequestType(),
        status = ServiceStatus.ERROR,
        errorMessage = ex.message
    )

    fun notFoundGatewayResponse(ex: NoSuchElementException) = GatewayResponse(
        MDC.get(MdcFields.REQUEST_ID.toString()),
        applicationDate.offsetDateTime(),
        MDC.get(MdcFields.REQUEST_TYPE.toString()).gatewayRequestType(),
        status = ServiceStatus.NOT_FOUND,
        errorMessage = ex.message
    )
}