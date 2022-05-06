package com.mendor71.order.gateway.testconfiguration

import com.fasterxml.jackson.databind.ObjectMapper
import com.mendor71.order.gateway.KafkaAuditService
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap
import org.testcontainers.utility.DockerImageName
import java.util.*
import kotlin.collections.HashMap

@Configuration
class KafkaAuditServiceContextConfiguration {
    @Value("\${audit.topic.request}")
    private lateinit var requestTopic: String
    @Value("\${audit.topic.response}")
    private lateinit var responseTopic: String

    @Bean
    fun container(): KafkaContainer {
        val container = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"))
        container.start()
        return container
    }

    @Bean
    fun testProducerFactory(): ProducerFactory<String, String> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = container().bootstrapServers
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun testKafkaTemplate() = KafkaTemplate(testProducerFactory())

    @Bean
    fun kafkaAuditService() = KafkaAuditService(testKafkaTemplate(), ObjectMapper().writer())

    @Bean
    fun kafkaRequestConsumer(): KafkaConsumer<String, String> {
        val consumer = KafkaConsumer(
            ImmutableMap.of<String, Any>(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, container().bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, "tc-" + UUID.randomUUID(),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
            ),
            StringDeserializer(),
            StringDeserializer()
        )
        consumer.subscribe(listOf(requestTopic))
        return consumer
    }

    @Bean
    fun kafkaResponseConsumer(): KafkaConsumer<String, String> {
        val consumer = KafkaConsumer(
            ImmutableMap.of<String, Any>(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, container().bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, "tc-" + UUID.randomUUID(),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
            ),
            StringDeserializer(),
            StringDeserializer()
        )
        consumer.subscribe(listOf(responseTopic))
        return consumer
    }
}