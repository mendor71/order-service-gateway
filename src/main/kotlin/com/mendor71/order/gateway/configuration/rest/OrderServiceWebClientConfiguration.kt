package com.mendor71.order.gateway.configuration.rest

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.time.Duration

@Component
@ConfigurationProperties("rest.client.order-service")
class OrderServiceWebClientConfiguration {
    lateinit var host: String
    lateinit var readTimeout: Duration
    lateinit var connectionTimeout: Duration
}