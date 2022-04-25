package com.mendor71.order.gateway.configuration

import com.mendor71.order.gateway.configuration.rest.OrderServiceWebClientConfiguration
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.util.concurrent.TimeUnit

@Configuration
class WebClientConfiguration {

    @Bean
    fun webClientBuilder(): WebClient.Builder = WebClient.builder()

    @Bean
    fun orderServiceWebClient(config: OrderServiceWebClientConfiguration): WebClient = webClientBuilder()
        .clientConnector(
            ReactorClientHttpConnector(
                HttpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.connectionTimeout.toMillis().toInt())
                    .doOnConnected {
                        it.addHandlerLast(ReadTimeoutHandler(config.readTimeout.toMillis(), TimeUnit.MILLISECONDS))
                    }
            )
        )
        .baseUrl(config.host)
        .defaultHeader(
            org.springframework.http.HttpHeaders.CONTENT_TYPE,
            MediaType.APPLICATION_JSON_VALUE
        )
        .build()
}