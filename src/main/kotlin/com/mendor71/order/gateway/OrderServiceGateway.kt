package com.mendor71.order.gateway

import com.mendor71.order.model.transfer.TransferOrder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class OrderServiceGateway(
    private val orderServiceClient: OrderServiceAsyncWebClient,
    private val auditService: KafkaAuditService<TransferOrder, Long>
) {
    @Value("\${audit.topic}")
    private lateinit var auditTopic: String

    suspend fun handleRequest(transferOrder: TransferOrder): ServiceResult =
        auditService.handleWithAudit(
            auditTopic,
            transferOrder,
            orderServiceClient::createOrder
        )
}