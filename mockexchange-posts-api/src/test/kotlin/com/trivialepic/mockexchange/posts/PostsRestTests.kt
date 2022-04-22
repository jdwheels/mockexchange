package com.trivialepic.mockexchange.posts

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.reactive.function.client.WebClient

@SpringBootTest
@AutoConfigureMockMvc
class PostsRestTests {

//    @Au
//    lateinit var client: WebTestClient
    @Autowired
    lateinit var mockMvc: MockMvc
//
//    @Autowired
//    fun create(ctx: WebApplicationContext) {
//        client = MockMvcWebTestClient.bindToApplicationContext(ctx)
//            .apply(springSecurity())
//            .defaultRequest(
//                MockMvcRequestBuilders.get("/").with(SecurityMockMvcRequestPostProcessors.oauth2Client()))
//                .configureClient()
//                .build();
//    }

    @Test
    fun works() {
        mockMvc.perform(get("/api/mockPosts").with(oauth2Login())).andExpect(status().isOk)
//        client.mutateWith(mockOAuth2Login()).get()
//            .uri { it.path("/api/mockPosts").build() }
//            .exchange()
//            .expectStatus().is2xxSuccessful
    }

//    @TestConfiguration
//    internal class WebClientConfig {
//        @Bean
//        fun webz(): WebClient {
//            return WebClient.create(web.url("/").toString())
//        }
//    }
}
