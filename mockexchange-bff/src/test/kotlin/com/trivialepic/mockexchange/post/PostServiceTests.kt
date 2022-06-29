package com.trivialepic.mockexchange.post

import com.trivialepic.mockexchange.objects.posts.PostSummary
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Import
import org.springframework.hateoas.MediaTypes.HAL_JSON
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.PagedModel.PageMetadata
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.test.StepVerifier
import java.io.IOException
import java.time.LocalDateTime

@SpringBootTest
@Import(PostService::class)
@EnableCaching
@Testcontainers
@DirtiesContext
//@ContextConfiguration(classes = [PostClientConfig::class])
class PostServiceTests {

    companion object {

        @Container
        var redis: GenericContainer<*> = GenericContainer("redis")
            .withExposedPorts(6379);

        @JvmStatic
        lateinit var mockBackEnd: MockWebServer

        init {
//            BlockHound.install()
        }

        val dispatcher: Dispatcher = object : Dispatcher() {
            @Throws(InterruptedException::class)
            override fun dispatch(request: RecordedRequest): MockResponse {
                when (request.path) {
                    "/posts/questions?size=20&page=0" -> {
                        return MockResponse()
                            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                            .setBody("""
                                {
                                   "_embedded": {
                                        "postSummaryList": [
                                            {
                                                "id": 1,
                                                "lastEditDate":"2010-09-12T12:46:38.027"
                                            }
                                        ]
                                    },
                                    "page": {
                                        "number": 0,
                                        "size": 20,
                                        "totalElements": 0,
                                        "totalPages": 0
                                    }
                                }
                                """.trimIndent())
                            .setResponseCode(200)
                    }
                    "/posts/questions/1" -> {
                        return MockResponse()
                            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                            .setBody("""
                                { "id": 1 } 
                            """.trimIndent())
                            .setResponseCode(HttpStatus.OK.value())
                    }
                    "/posts/answers?parentId=1" -> {
                        return MockResponse()
                            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                            .setBody("""
                                [ 
                                    { "id": 3 }, 
                                    { "id": 4 }, 
                                    { "id": 5 } 
                                ]
                                """.trimIndent())
                            .setResponseCode(HttpStatus.OK.value())
                    }
                }
                return MockResponse().setResponseCode(404)
            }
        }

        @BeforeAll
        @JvmStatic
        @Throws(IOException::class)
        fun setUp() {
            mockBackEnd = MockWebServer()
            mockBackEnd.dispatcher = dispatcher
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
            registry.add("bff-api.post-api-base-url") {
                "http://${mockBackEnd.hostName}:${mockBackEnd.port}"
            }
            registry.add("spring.redis.host", redis::getHost)
            registry.add("spring.redis.port", redis::getFirstMappedPort)
        }
    }

    private var initialRequestCount = 0

    @BeforeEach
    fun setInitialRequestCount() {
        initialRequestCount = mockBackEnd.requestCount
    }

    @Autowired
    private lateinit var service: PostService

    @Test
    fun works() {
        Assertions.assertNotNull(service)
    }

    @Test
    fun returns() {
        val expected = PagedModel.of(listOf(PostSummary(id = 1, lastEditDate = LocalDateTime.of(2010, 9, 12, 12, 46, 38, 27_000_000))), PageMetadata(20, 0, 0, 0))

        StepVerifier.create(service.getQuestions(20, 0))
            .expectNext(expected)
            .verifyComplete()

        Assertions.assertEquals(initialRequestCount + 1, mockBackEnd.requestCount)

        StepVerifier.create(service.getQuestions(20, 0))
            .expectNext(expected)
            .verifyComplete()

        Assertions.assertEquals(initialRequestCount + 1, mockBackEnd.requestCount)

    }

    @Test
    fun returnsQuestion() {
        StepVerifier.create(service.getQuestion(1)).expectNext(PostSummary(id = 1)).verifyComplete()

        Assertions.assertEquals(initialRequestCount + 1, mockBackEnd.requestCount)
    }

    @Test
    fun returnsAnswers() {
        StepVerifier.create(service.getAnswers(1))
            .expectNext(
                PostSummary(id = 3),
                PostSummary(id = 4),
                PostSummary(id = 5)
            )
            .verifyComplete()

        Assertions.assertEquals(initialRequestCount + 1, mockBackEnd.requestCount)
    }

    @Test
    fun clears() {
        StepVerifier.create(service.clear()).expectNext().verifyComplete()
    }

}
