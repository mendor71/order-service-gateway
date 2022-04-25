package com.mendor71.order.gateway.utils

import com.mendor71.order.model.gateway.GatewayRequestType.Companion.gatewayRequestType
import com.mendor71.order.model.gateway.GatewayResponse
import com.mendor71.order.model.gateway.ServiceStatus
import org.slf4j.MDC

fun Any.okGatewayResponse() = GatewayResponse(
    MDC.get(MdcFields.REQUEST_ID.toString()),
    MDC.get(MdcFields.REQUEST_TYPE.toString()).gatewayRequestType(),
    status = ServiceStatus.OK,
    body = this
)

fun Throwable.errorGatewayResponse() = GatewayResponse(
    MDC.get(MdcFields.REQUEST_ID.toString()),
    MDC.get(MdcFields.REQUEST_TYPE.toString()).gatewayRequestType(),
    status = ServiceStatus.ERROR,
    errorMessage = message
)

fun NoSuchElementException.notFoundGatewayResponse() = GatewayResponse(
    MDC.get(MdcFields.REQUEST_ID.toString()),
    MDC.get(MdcFields.REQUEST_TYPE.toString()).gatewayRequestType(),
    status = ServiceStatus.NOT_FOUND,
    errorMessage = message
)