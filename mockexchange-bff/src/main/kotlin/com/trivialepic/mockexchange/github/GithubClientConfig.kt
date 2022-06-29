package com.trivialepic.mockexchange.github

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.client.web.server.UnAuthenticatedServerOAuth2AuthorizedClientRepository
import org.springframework.util.MimeType
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class GithubClientConfig {

    companion object {

        const val GITHUB_CONTENT_TYPE = "application/vnd.github.v3+json"
    }
    @Bean
    fun authorizedClientManager(
        clientRegistrationRepository: ReactiveClientRegistrationRepository,
        authorizedClientRepository: ServerOAuth2AuthorizedClientRepository
    ): ReactiveOAuth2AuthorizedClientManager {
        val authorizedClientProvider: ReactiveOAuth2AuthorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
            .authorizationCode()
            .refreshToken()
            .clientCredentials()
            .password()
            .build()
        val authorizedClientManager = DefaultReactiveOAuth2AuthorizedClientManager(
            clientRegistrationRepository, authorizedClientRepository)
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider)
        return authorizedClientManager
    }

    @Bean
    fun githubClient(clientManager: ReactiveOAuth2AuthorizedClientManager): WebClient? {
        val oauth = ServerOAuth2AuthorizedClientExchangeFilterFunction(clientManager)
//        oauth.setDefaultClientRegistrationId(CommonOAuth2Provider.GITHUB.name.lowercase())
        return WebClient.builder()
//            .filter(oauth)
            .baseUrl("https://api.github.com")
            .defaultHeader(HttpHeaders.ACCEPT, GITHUB_CONTENT_TYPE)
            .build()
    }
}
