package com.trivialepic.mockexchange.posts

import com.trivialepic.mockexchange.objects.posts.MockPost
import com.trivialepic.mockexchange.objects.posts.MockPostType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@DataJpaTest
@EnableJpaAuditing
class PostsRepositoryTests {

    @Autowired
    lateinit var repository: PostsRepository

    @Test
    fun works() {
        Assertions.assertTrue(repository.findAll().isEmpty())
    }

    @Test
    fun sequences() {
        val q1 = repository.save(MockPost().apply {
            postType = MockPostType.ANSWER
            body = ""
        })
        Assertions.assertEquals(1, q1.id)

        val a1 = repository.save(MockPost().apply {
            postType = MockPostType.ANSWER
            body = ""
        })
        Assertions.assertEquals(2, a1.id)

        val a2 = repository.save(MockPost().apply {
            id = 5
            postType = MockPostType.ANSWER
            body = ""
        })
        Assertions.assertEquals(5, a2.id)
    }

    @Test
    fun filters() {
        val posts = repository.saveAll(listOf(
            MockPost().apply {
                body = "Testing"
                contentLicense = "CC BY-SA 2.5"
                title = "Does this pass?"
                postType = MockPostType.QUESTION
            },
            MockPost().apply {
                body = "Testing"
                contentLicense = "CC BY-SA 2.5"
                postType = MockPostType.ANSWER
            },
            MockPost().apply {
                body = "Testing"
                contentLicense = "CC BY-SA 2.5"
                postType = MockPostType.ANSWER
            }
        ))
        Assertions.assertEquals(3, posts.size)

        val answers = repository.x(MockPostType.ANSWER, Pageable.unpaged())
        Assertions.assertEquals(2, answers.totalElements)

        val questions = repository.x(MockPostType.QUESTION, Pageable.unpaged())
        Assertions.assertEquals(1, questions.totalElements)
    }
}
