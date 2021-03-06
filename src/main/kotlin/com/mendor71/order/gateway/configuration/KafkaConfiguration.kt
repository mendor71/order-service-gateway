package com.mendor71.order.gateway.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.datatype.jsr310.JSR310Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory


@Configuration
class KafkaConfiguration {
    @Value(value = "\${audit.kafka.bootstrap-address}")
    private lateinit var bootstrapAddress: String

    @Bean
    fun producerFactory(): ProducerFactory<String, String>? {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String?, String?>? {
        return KafkaTemplate(producerFactory())
    }

    @Bean
    fun objectWriter(): ObjectWriter = ObjectMapper()
        .registerModule(JavaTimeModule())
        .writer()
}