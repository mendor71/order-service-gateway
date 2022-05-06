package com.mendor71.order.gateway

import com.mendor71.order.gateway.testconfiguration.KafkaAuditServiceContextConfiguration
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.rnorth.ducttape.unreliables.Unreliables
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import java.time.Duration
import java.util.concurrent.TimeUnit


/**
 * WARNING!
 * May take several minutes on first run to load kafka testcontainer docker image
 * */
@DirtiesContext
@SpringBootTest(classes = [KafkaAuditServiceContextConfiguration::class])
class KafkaAuditServiceTest {
    @Autowired
    lateinit var kafkaAuditService: KafkaAuditService

    @Autowired
    lateinit var kafkaRequestConsumer: KafkaConsumer<String, String>

    @Autowired
    lateinit var kafkaResponseConsumer: KafkaConsumer<String, String>

    @Test
    fun `test logRequest`() {
        kafkaAuditService.logRequest("TEST_REQUEST", "audit message")

        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS) {
            val records: ConsumerRecords<String, String> = kafkaRequestConsumer.poll(Duration.ofMillis(100))
            if (records.isEmpty) {
                return@retryUntilTrue false
            }
            assertThat(records).hasSize(1)
            assertThat(
                records.first().value()
            ).isEqualTo("{\"auditPoint\":\"TEST_REQUEST\",\"payload\":\"audit message\"}")
            true
        }
        kafkaRequestConsumer.unsubscribe()
    }

    @Test
    fun `test logResponse`() {
        kafkaAuditService.logResponse("TEST_RESPONSE", "audit message")

        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS) {
            val records: ConsumerRecords<String, String> = kafkaResponseConsumer.poll(Duration.ofMillis(100))
            if (records.isEmpty) {
                return@retryUntilTrue false
            }
            assertThat(records).hasSize(1)
            assertThat(
                records.first().value()
            ).isEqualTo("{\"auditPoint\":\"TEST_RESPONSE\",\"payload\":\"audit message\"}")
            true
        }
        kafkaResponseConsumer.unsubscribe()
    }
}