package com.trivialepic.mockexchange.post

import com.ninjasquad.springmockk.MockkBean
import com.trivialepic.mockexchange.BffSecurityConfig
import com.trivialepic.mockexchange.github.GithubService
import com.trivialepic.mockexchange.objects.posts.PostSummary
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.hateoas.PagedModel
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@ActiveProfiles("test")
@Suppress("ReactiveStreamsUnusedPublisher")
@WebFluxTest(PostController::class)
@Import(BffSecurityConfig::class)
class PostControllerTests {

    @MockkBean
    lateinit var postService: PostService

    @Autowired
    lateinit var client: WebTestClient

    @MockkBean
    lateinit var githubService: GithubService

    @Test
    @WithMockUser(username = "tester")
    fun works() {
        every { postService.getQuestions(any(), any()) } returns Mono.just(
            PagedModel.of(listOf(mockk<PostSummary>(relaxed = true)), PagedModel.PageMetadata(20, 0, 0, 0)))
        client
//            .mutateWith(mockOAuth2Login())
            .get()
            .uri("/posts/questions")
            .exchange()
            .expectStatus().isOk
        verify { postService.getQuestions(20, 0) }
    }

    @Test
    @WithMockUser
    fun getQuestion() {
        every { postService.getQuestion(any()) } returns Mono.just(PostSummary(id = 2))

        client.get()
            .uri("/posts/questions/2")
            .exchange()
            .expectStatus().isOk
            .expectBody().json("{ \"id\": 2 }")

        verify { postService.getQuestion(2) }
    }

    @Test
    @WithMockUser
    fun getAnswers() {
        every { postService.getAnswers(any()) } returns Flux.just(PostSummary(id = 3), PostSummary(id = 4), PostSummary(id = 5))

        client.get()
            .uri("/posts/answers?parentId=2")
            .exchange()
            .expectStatus().isOk
            .expectBody().json("""
                [{ "id":  3 }, { "id":  4 }, { "id":  5 }]
            """.trimIndent())

        verify { postService.getAnswers(2) }
    }

    @Test
    @WithMockUser
    fun clear() {
        every { postService.clear() } returns Mono.fromRunnable {}

        client.get()
            .uri("/posts/clear")
            .exchange()
            .expectStatus().isNoContent

        verify { postService.clear() }
    }
}
