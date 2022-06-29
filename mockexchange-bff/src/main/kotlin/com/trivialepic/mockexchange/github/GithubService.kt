package com.trivialepic.mockexchange.github

import org.springframework.http.HttpHeaders
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import reactor.core.publisher.Flux

@Service
class GithubService(private val githubClient: WebClient) {

    fun getEmails(acessToken: OAuth2AccessToken): Flux<GithubEmail> {
        return githubClient.get().uri("/user/emails")
            .header(HttpHeaders.AUTHORIZATION, "${acessToken.tokenType.value} ${acessToken.tokenValue}")
            .retrieve()
            .bodyToFlux<GithubEmail>()
    }
}
