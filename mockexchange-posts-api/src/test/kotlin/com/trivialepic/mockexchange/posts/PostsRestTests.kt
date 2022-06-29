@file:Suppress("ReactiveStreamsUnusedPublisher")

package com.trivialepic.mockexchange.posts

import com.ninjasquad.springmockk.MockkBean
import com.trivialepic.mockexchange.objects.posts.MockPost
import com.trivialepic.mockexchange.objects.posts.MockPostType
import com.trivialepic.mockexchange.objects.posts.PostSummary
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.hateoas.HateoasProperties
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.data.web.config.HateoasAwareSpringDataWebConfiguration
import org.springframework.hateoas.config.HateoasConfiguration
import org.springframework.hateoas.mediatype.hal.HalMediaTypeConfiguration
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@EnableSpringDataWebSupport
@WebFluxTest(excludeAutoConfiguration = [FlywayAutoConfiguration::class, R2dbcAutoConfiguration::class])
@EnableConfigurationProperties(PostsApiProperties::class)
@Import(PostsApiHandler::class, PostsApiRouter::class, PostsSecurityConfig::class)
class PostsRestTests {

    @MockkBean
    lateinit var postsRepository: PostsRepository

    @MockkBean
    lateinit var xabcRepository: XabcRepository
//
//    @MockkBean
//    lateinit var postsApiHandler: PostsApiHandler

    @Autowired
    lateinit var client: WebTestClient

    @Test
    fun works() {
        every { postsRepository.findQuestions(0, 1) } returns Flux.just(mockkClass(PostSummary::class, relaxed = true))
        every { postsRepository.countByPostType(MockPostType.QUESTION) } returns Mono.just(2L)

        client.get()
            .uri("/posts/questions?page=0&size=1")
            .exchange()
            .expectStatus().isOk
            .expectBody()
                .jsonPath("$.content").isArray
                .jsonPath("$.page.totalPages").isEqualTo(2)

        verify { postsRepository.findQuestions(0, 1) }
        verify { postsRepository.countByPostType(MockPostType.QUESTION) }
    }

//    @Test
//    fun auth() {
//        client.get().uri("/posts/questions").exchange().expectStatus().isUnauthorized
//    }

    @Test
    fun badRequest() {
        client
            .get().uri("/posts/answers").exchange().expectStatus().isBadRequest
    }

    @Test
    fun createPost() {
        every { postsRepository.save(any()) } returns Mono.just(mockk(relaxed = true) {
            every { id } returns 1
        })
        client.mutateWith(csrf())
            .post().uri("/posts/questions")
            .bodyValue(QuestionRequest("Test", "this is a test"))
            .exchange()
            .expectStatus().isCreated
            .expectHeader().location("/posts/questions/1")
            .expectBody(MockPost::class.java)
    }

    @Test
    fun createBadPost() {}

}
