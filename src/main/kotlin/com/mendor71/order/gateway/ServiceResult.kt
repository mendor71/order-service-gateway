package com.mendor71.order.gateway

data class ServiceResult(
    val status: ServiceStatus,
    val response: Any? = null,
    val errorMessage: String? = null
) {
    companion object {
        fun ok(value: Any?) = ServiceResult(status = ServiceStatus.OK, response = value)
        fun error(message: String) = ServiceResult(status = ServiceStatus.ERROR, errorMessage = message)
    }
}

enum class ServiceStatus {
    OK, ERROR
}