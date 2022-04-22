package com.mendor71.order.gateway

import com.fasterxml.jackson.databind.ObjectWriter
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaAuditService<T, R>(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectWriter: ObjectWriter
) {
    suspend fun handleWithAudit(topic: String, request: T, handler: suspend (T) -> R): ServiceResult {
        kafkaTemplate.send(topic, objectWriter.writeValueAsString(request))
        return try {
            ServiceResult.ok(handler(request))
        } catch (e: Exception) {
            ServiceResult.error(e.message ?: "something went wrong")
        }
    }
}