package com.trivialepic.mockexchange.post

import com.trivialepic.mockexchange.BffProperties
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.SslProvider
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
@EnableConfigurationProperties(BffProperties::class)
class PostClientConfig(private val bffProperties: BffProperties) {

    val httpClient = HttpClient.create().secure { it.sslContext(SslContextBuilder.forClient().sslProvider(SslProvider.OPENSSL).build()) }

    @Bean
    fun postApiClient() = WebClient.builder()
        .clientConnector(ReactorClientHttpConnector(httpClient))
        .baseUrl(bffProperties.postApiBaseUrl).build()
}
