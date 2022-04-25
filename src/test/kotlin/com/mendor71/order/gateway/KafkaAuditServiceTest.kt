package com.mendor71.order.gateway

import com.mendor71.order.gateway.testconfiguration.ContextConfiguration
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

@DirtiesContext
@SpringBootTest(classes = [ContextConfiguration::class])
class KafkaAuditServiceTest {
    @Autowired
    lateinit var kafkaAuditService: KafkaAuditService

    @Autowired
    lateinit var testKafkaConsumer: KafkaConsumer<String, String>

    @Test
    fun `test logMessage`() {
        kafkaAuditService.logMessage("TEST_AUDIT_POINT", "audit message")

        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS) {
            val records: ConsumerRecords<String, String> = testKafkaConsumer.poll(Duration.ofMillis(100))
            if (records.isEmpty) {
                return@retryUntilTrue false
            }
            assertThat(records).hasSize(1)
            assertThat(
                records.first().value()
            ).isEqualTo("{\"auditPoint\":\"TEST_AUDIT_POINT\",\"payload\":\"audit message\"}")
            true
        }
        testKafkaConsumer.unsubscribe()
    }
}