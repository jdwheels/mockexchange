package com.trivialepic.mockexchange

import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class BffUserDetailsService(
    private val userApiClient: WebClient, private val githubClient: WebClient) : ReactiveUserDetailsService {

    override fun findByUsername(username: String): Mono<UserDetails> {
        return userApiClient.get().uri("/users/{id}", username).exchangeToMono {
            if (it.statusCode().is2xxSuccessful) {
                Mono.just(User.builder().username(username).password("{noop}x").authorities(listOf()).build())
            } else {
                it.createException().flatMap { Mono.error(it.rootCause!!) }
            }
        }
    }
}
