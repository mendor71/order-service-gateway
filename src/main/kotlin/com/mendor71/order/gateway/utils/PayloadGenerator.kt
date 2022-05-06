package com.mendor71.order.gateway.utils

import com.fasterxml.jackson.databind.ObjectWriter
import com.mendor71.order.model.dto.PositionDto
import com.mendor71.order.model.dto.SalePointDto
import com.mendor71.order.model.dto.StatusDto
import com.mendor71.order.model.gateway.GatewayRequest
import com.mendor71.order.model.gateway.GatewayRequestType
import com.mendor71.order.model.gateway.GatewayResponse
import com.mendor71.order.model.gateway.ServiceStatus
import com.mendor71.order.model.gateway.order.GetOrderResponse
import com.mendor71.order.model.transfer.TransferOrder
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.UUID
import javax.annotation.PostConstruct

@Component
class PayloadGenerator(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectWriter: ObjectWriter,
    private val dispatcher: CoroutineDispatcher
) {
    @Value("\${audit.topic.request}")
    private lateinit var auditRequestTopic: String

    @Value("\${audit.topic.response}")
    private lateinit var auditResponseTopic: String
    private val logger: Logger = LoggerFactory.getLogger(this::class.simpleName)

    @PostConstruct
    fun generatePayload() {
        dispatcher.asExecutor().execute {
            while (true) {
                sendRequest()
                Thread.sleep(1000L)
            }
        }
        dispatcher.asExecutor().execute {
            while (true) {
                sendResponse()
                Thread.sleep(1000L)
            }
        }
    }

    fun sendResponse() {
        val message = objectWriter.writeValueAsString(
            GatewayResponse(
                requestId = UUID.randomUUID().toString(),
                responseTime = OffsetDateTime.now(),
                type = GatewayRequestType.GET_ORDER,
                status = ServiceStatus.OK,
                body = GetOrderResponse(
                    order = TransferOrder(
                        salePoint = SalePointDto(1, 1, "salePoint"),
                        status = StatusDto(1, "Order", "NEW"),
                        positions = listOf(
                            PositionDto(1, 1, "iPhoneXS", 1)
                        )
                    )
                )
            )
        )
        kafkaTemplate.send(auditResponseTopic, message).addCallback({ result ->
            logger.info(
                "Sent test message $message to $auditResponseTopic with offset=[" + result.recordMetadata.offset() + "]"
            )
        }, { ex ->
            logger.info(
                "Unable to send test to $auditResponseTopic message due to : " + ex.message
            )
        })
    }

    fun sendRequest() {
        val message = objectWriter.writeValueAsString(
            GatewayRequest(
                requestId = UUID.randomUUID().toString(),
                requestTime = OffsetDateTime.now(),
                type = GatewayRequestType.GET_ORDER,
                body = TransferOrder(
                    salePoint = SalePointDto(1, 1, "salePoint"),
                    status = StatusDto(1, "Order", "NEW"),
                    positions = listOf(
                        PositionDto(1, 1, "iPhoneXS", 1)
                    )
                )
            )
        )
        kafkaTemplate.send(auditRequestTopic, message).addCallback({ result ->
            logger.info(
                "Sent test message $message to $auditRequestTopic with offset=[" + result.recordMetadata.offset() + "]"
            )
        }, { ex ->
            logger.info(
                "Unable to send test $auditRequestTopic message due to : " + ex.message
            )
        })
    }
}