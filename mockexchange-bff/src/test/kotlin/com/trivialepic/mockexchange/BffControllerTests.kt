package com.trivialepic.mockexchange

import com.ninjasquad.springmockk.MockkBean
import com.trivialepic.mockexchange.github.GithubService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.Duration


@ActiveProfiles("test")
@WebFluxTest(properties = ["bff-api.cookie-domain=localhost"], controllers = [BffController::class])
@Import(BffSecurityConfig::class)
class BffControllerTests {

    companion object {
        const val SESSION_COOKIE_NAME = "SESSION"
    }

    @TestConfiguration
    class TestSessionConfig {

//        @Bean
//        fun reactiveSessionRepository() = ReactiveMapSessionRepository(ConcurrentHashMap())
    }

    @Autowired
    lateinit var client: WebTestClient

    @MockkBean
    lateinit var githubService: GithubService

    @Test
    fun `requires auth`() {
        client.get().uri("/")
            .exchange()
            .expectStatus().isUnauthorized
    }

    @Test
    fun `returns JSON`() {
        client.mutateWith(mockOAuth2Login())
            .get()
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectCookie().domain(SESSION_COOKIE_NAME, "localhost")
            .expectCookie().path(SESSION_COOKIE_NAME, "/")
            .expectCookie().maxAge(SESSION_COOKIE_NAME, Duration.ofSeconds(1).negated())
            .expectBody()
                .jsonPath("$[0].y").isEqualTo("a")
                .json("""[{"y": "a"}, {"y": "b"}, {"y": "c"}]""")
    }

}
