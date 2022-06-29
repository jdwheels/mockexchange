package com.trivialepic.mockexchange.posts

import com.trivialepic.mockexchange.objects.posts.MockPost
import com.trivialepic.mockexchange.objects.posts.MockPostType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.core.publisher.Flux
import reactor.kotlin.test.test


@DataR2dbcTest
@Testcontainers
@Import(PostsDataConfig::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostsRepositoryTests {

    companion object {
        @Container
        var postgres: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:14.1")
            .withDatabaseName("prop")
            .withUsername("postgres")
            .withPassword("pass")
            .withExposedPorts(5432)

        @JvmStatic
        @DynamicPropertySource
        fun registerPgProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.flyway.url", postgres::getJdbcUrl)
            registry.add("spring.flyway.password", postgres::getPassword)
            registry.add("spring.flyway.user", postgres::getUsername)

            // R2DBC DataSource Example
            registry.add("spring.r2dbc.url") {
                with(postgres) {
                    "r2dbc:pool:postgresql://${host}:${firstMappedPort}/${databaseName}"
                }
            }
            registry.add("spring.r2dbc.username", postgres::getUsername)
            registry.add("spring.r2dbc.password", postgres::getPassword)
        }
    }

    @Autowired
    lateinit var repository: PostsRepository

    @Test
    fun sequences() {
        val q1 = repository.save(MockPost().apply {
            postType = MockPostType.ANSWER
            postTypeId = MockPostType.ANSWER.ordinal
            body = ""
            contentLicense  = "CC BY-SA 2.5"
        })
        q1.test()
            .expectNextMatches { it.id == 1_000_000_001L }
            .verifyComplete()
//        Assertions.assertEquals(1, q1.id)
//
//        val a1 = repository.save(MockPost().apply {
//            postType = MockPostType.ANSWER
//            body = ""
//        })
//        Assertions.assertEquals(2, a1.id)
//
//        val a2 = repository.save(MockPost().apply {
//            id = 5
//            postType = MockPostType.ANSWER
//            body = ""
//        })
//        Assertions.assertEquals(5, a2.id)
    }

    @Test
    fun filters() {
        val post = repository.save(
            MockPost().apply {
                body = "Testing"
                contentLicense = "CC BY-SA 2.5"
                title = "Does this pass?"
                postType = MockPostType.QUESTION
                postTypeId = MockPostType.QUESTION.ordinal
            },
        ).share()

        post.test().expectNextCount(1).verifyComplete()


        val a = post
            .flatMapMany {Flux.just(
                MockPost().apply {
                    parentId = it.id
                    body = "Testing"
                    contentLicense = "CC BY-SA 2.5"
                    postType = MockPostType.ANSWER
                    postTypeId = MockPostType.ANSWER.ordinal
                },
                MockPost().apply {
                    parentId = it.id
                    body = "Testing"
                    contentLicense = "CC BY-SA 2.5"
                    postType = MockPostType.ANSWER
                    postTypeId = MockPostType.ANSWER.ordinal
                }
            )}
            .transform {
                repository.saveAll(it)
            }
        a.test().expectNextCount(2).verifyComplete()

        val answers = post.flatMapMany { repository.findAnswers(it.id!!) }
        answers.test().expectNextCount(2).verifyComplete()

        val questions = repository.findQuestions(0, 20)
        questions.test().expectNextCount(1).verifyComplete()
    }


    @Test
    fun updates() {
        val newPostSaved = repository.save(MockPost(body = "x", postTypeId = 1, postType = MockPostType.QUESTION)).share()

        newPostSaved.test()
            .assertNext {
                Assertions.assertNotNull(it.creationDate)
                Assertions.assertNotNull(it.lastActivityDate)
                Assertions.assertEquals(it.creationDate, it.lastActivityDate)
            }
            .verifyComplete()

        val newCreationDate = newPostSaved.mapNotNull(MockPost::creationDate)
        val newActivityDate = newPostSaved.mapNotNull(MockPost::lastActivityDate)

        val newPostDates = newCreationDate.zipWith(newActivityDate)
        val updatedPost = newPostSaved.map { it.copy(body = "y") }
        val updatedPostSaved = updatedPost.flatMap(repository::save).share()

        updatedPostSaved.test()
            .assertNext {
                Assertions.assertNotNull(it.creationDate)
                Assertions.assertNotNull(it.lastActivityDate)
                Assertions.assertTrue(it.creationDate!!.isBefore(it.lastActivityDate))
            }
            .verifyComplete()

        val updatedCreationDate = updatedPostSaved.mapNotNull(MockPost::creationDate)
        val updatedActivityDate = updatedPostSaved.mapNotNull(MockPost::lastActivityDate)
        val updatedPostDates = updatedCreationDate.zipWith(updatedActivityDate)

        newPostDates.zipWith(updatedPostDates).test()
            .assertNext { x ->
                val new = x.t1
                val updated = x.t2
                Assertions.assertEquals(new.t1, updated.t1)
                Assertions.assertTrue(new.t2.isBefore(updated.t2))
            }
            .verifyComplete()

    }
}
