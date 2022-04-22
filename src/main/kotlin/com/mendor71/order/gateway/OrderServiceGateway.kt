package com.mendor71.order.gateway

import com.mendor71.order.model.transfer.TransferOrder
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.concurrent.TimeUnit

@Component
@ConfigurationProperties("rest.client.order-service")
class OrderServiceGateway(
    private val webClientBuilder: WebClient.Builder
) : InitializingBean {
    lateinit var webClient: WebClient

    lateinit var host: String
    lateinit var uri: String
    lateinit var readTimeout: Duration
    lateinit var connectionTimeout: Duration

    override fun afterPropertiesSet() {
        webClient = webClientBuilder
            .clientConnector(
                ReactorClientHttpConnector(
                    HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout.toMillis().toInt())
                        .doOnConnected {
                            it.addHandlerLast(ReadTimeoutHandler(readTimeout.toMillis(), TimeUnit.MILLISECONDS))
                        }
                )
            )
            .baseUrl(host)
            .defaultHeader(
                org.springframework.http.HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_JSON_VALUE
            )
            .build()
    }

    suspend fun createOrder(order: TransferOrder): Long =
        webClient
            .post()
            .uri(uri)
            .body(BodyInserters.fromValue(order))
            .retrieve()
            .bodyToMono(Long::class.java)
            .awaitSingle()
}