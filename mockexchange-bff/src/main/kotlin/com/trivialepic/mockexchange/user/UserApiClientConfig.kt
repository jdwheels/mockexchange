package com.trivialepic.mockexchange.user

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(UserApiProperties::class)
class UserApiClientConfig(private val properties: UserApiProperties) {

    @Bean
    fun userApiClient(): WebClient {
        return WebClient.builder().baseUrl(properties.baseUrl).build()
    }
}
