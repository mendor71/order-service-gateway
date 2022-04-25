package com.mendor71.order.gateway

import com.fasterxml.jackson.databind.ObjectWriter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "audit", name = ["enabled"], havingValue = "true")
class KafkaAuditService(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectWriter: ObjectWriter
) : IAuditService {
    @Value("\${audit.topic}")
    private lateinit var auditTopic: String
    override val logger: Logger = LoggerFactory.getLogger(this::class.simpleName)

    override fun logMessage(auditPoint: String, message: Any) =
        kafkaTemplate.send(auditTopic, objectWriter.writeValueAsString(AuditMessage(auditPoint, message)))
            .addCallback({ result ->
                logger.debug(
                    "Sent message=[" + message + "] with offset=[" + result.recordMetadata.offset() + "]"
                )
            }, { ex ->
                logger.debug(
                    "Unable to send message=[" + message + "] due to : " + ex.message
                )
            })
}