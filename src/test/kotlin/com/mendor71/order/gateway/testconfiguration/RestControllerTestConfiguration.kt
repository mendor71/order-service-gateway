package com.mendor71.order.gateway.testconfiguration

import com.mendor71.order.gateway.order.CreateOrderGateway
import com.mendor71.order.gateway.order.GetOrderGateway
import io.mockk.mockk
import kotlinx.coroutines.asCoroutineDispatcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors

@Configuration
class RestControllerTestConfiguration {

    @Bean
    fun createGateway() = mockk<CreateOrderGateway>()

    @Bean
    fun getGateway() = mockk<GetOrderGateway>()

    @Bean
    fun commonDispatcher() = Executors.newCachedThreadPool().asCoroutineDispatcher()

}