package com.trivialepic.mockexchange

import com.ninjasquad.springmockk.MockkBean
import com.trivialepic.mockexchange.user.UserApiClientConfig
import com.trivialepic.mockexchange.user.UserApiProperties
import io.mockk.every
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier
import java.io.IOException


@ActiveProfiles("test")
@SpringBootTest(classes = [])
@Import(BffUserDetailsService::class, UserApiClientConfig::class)
class BffUserDetailsServiceTests {

    companion object {

        @JvmStatic
        lateinit var mockBackEnd: MockWebServer

        @BeforeAll
        @JvmStatic
        @Throws(IOException::class)
        fun setUp() {
            mockBackEnd = MockWebServer()
            mockBackEnd.start()
        }

        @AfterAll
        @JvmStatic
        @Throws(IOException::class)
        fun tearDown() {
            mockBackEnd.shutdown()
        }

        @JvmStatic
        @DynamicPropertySource
        fun dynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("mockexchange-bff.user-api.base-url") { "http://${mockBackEnd.hostName}:${mockBackEnd.port}" }
        }
    }

//    @MockkBean
//    lateinit var userApiClient: WebClient

    @Autowired
    lateinit var service: BffUserDetailsService

    @Test
    fun works() {
        mockBackEnd.enqueue(MockResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).setResponseCode(200).setBody("{}"))
        StepVerifier.create(service.findByUsername("x")).expectNextCount(1).verifyComplete()
    }
}
