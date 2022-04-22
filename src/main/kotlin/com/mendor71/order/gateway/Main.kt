package com.mendor71.order.gateway

import org.springframework.boot.Banner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.EnableConfigurationProperties

@EnableConfigurationProperties
@SpringBootApplication
class Main

fun main() {
    SpringApplicationBuilder(Main::class.java)
        .web(WebApplicationType.REACTIVE)
        .bannerMode(Banner.Mode.OFF)
        .run()
}