package com.trivialepic.mockexchange.user

import com.ninjasquad.springmockk.MockkBean
//import com.trivialepic.mockexchange.BffSecurityConfig
import com.trivialepic.mockexchange.github.GithubService
import io.mockk.Called
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.server.adapter.WebHttpHandlerBuilder.SpringWebBlockHoundIntegration

@Suppress("ReactiveStreamsUnusedPublisher")
@ActiveProfiles("test")
//@Import(BffSecurityConfig::class)
@WebFluxTest(controllers = [UserController::class])
class UserControllerTests {

    @MockkBean
    lateinit var githubService: GithubService

    @Autowired
    lateinit var client: WebTestClient

    @Test
    fun works() {
        client.mutateWith(mockOAuth2Login()).get()
            .uri("/user/x").exchange()
            .expectStatus().isOk
            .expectBody().json("{ \"user\": {} }")

        verify { githubService.getEmails(any()).wasNot(Called) }


    }
}
