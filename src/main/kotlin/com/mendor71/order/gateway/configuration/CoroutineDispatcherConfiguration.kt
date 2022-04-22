package com.mendor71.order.gateway.configuration

import kotlinx.coroutines.asCoroutineDispatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.CustomizableThreadFactory
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Configuration
class CoroutineDispatcherConfiguration {
    @Bean
    fun dispatcher() = ThreadPoolExecutor(
        10,
        30,
        5,
        TimeUnit.SECONDS,
        LinkedBlockingQueue(100),
        CustomizableThreadFactory("IO-thread-")
    ).asCoroutineDispatcher()
}