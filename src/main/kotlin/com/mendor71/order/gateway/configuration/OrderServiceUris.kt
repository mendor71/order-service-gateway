package com.mendor71.order.gateway.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("rest.client.order-service.uris")
class OrderServiceUris {
    lateinit var create: String
    lateinit var get: String
}