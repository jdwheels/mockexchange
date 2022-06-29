package com.trivialepic.mockexchange.user

import com.fasterxml.jackson.annotation.JsonValue
import com.trivialepic.mockexchange.github.GithubEmail
import com.trivialepic.mockexchange.github.GithubService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/user", produces = [MediaType.APPLICATION_JSON_VALUE])
class UserController {

    private lateinit var github: GithubService

    @Autowired
    fun setGithub(github: GithubService) {
        this.github = github
    }

    data class UserResponse(val attributes: Map<String, String>, val emails: List<GithubEmail>)

    @GetMapping(path = ["/x"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun userDetails(
//        auth: Authentication,
        @AuthenticationPrincipal principal: OAuth2User?,
//        @RegisteredOAuth2AuthorizedClient("github") authorizedClient: OAuth2AuthorizedClient
    ): Mono<Map<String, Any?>> {
//        return github.getEmails().collectList().map { mapOf(Pair("user", it)) }
        return Mono.just(mapOf(Pair("user", principal)))
    }
}
