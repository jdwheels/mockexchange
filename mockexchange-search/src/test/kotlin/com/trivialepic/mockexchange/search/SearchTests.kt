package com.trivialepic.mockexchange.search

import com.ninjasquad.springmockk.MockkBean
import com.trivialepic.mockexchange.objects.posts.MockPost
import com.trivialepic.mockexchange.objects.posts.MockPostType
import com.trivialepic.mockexchange.search.solr.SolrIngest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.web.util.UriComponentsBuilder
import org.testcontainers.containers.SolrContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName



@SpringBootTest
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class])
@Testcontainers
class SearchTests {

    companion object {
        @Container
        private val solr: SolrContainer = SolrContainer(DockerImageName.parse("solr:8"))
            .withCollection("posts")

        @JvmStatic
        @DynamicPropertySource
        fun registerPgProperties(registry: DynamicPropertyRegistry) {
            registry.add("mockexchange.solr.hosts") {
                UriComponentsBuilder.newInstance().scheme("http").host(solr.host).port(solr.solrPort).path("/solr").toUriString()
            }
        }
    }

    @MockkBean
    lateinit var postsRepository: PostsRepository

    @Autowired
    lateinit var ingest: SolrIngest

    @Test
    fun works() {
        val page = PageableExecutionUtils.getPage(
            listOf(mockk<MockPost> {
                every { id } returns 1
            }),
            Pageable.ofSize(100)
        ) { 0 }
        every { postsRepository.getAllByPostType(MockPostType.QUESTION, any()) } returns page
        ingest.x()
        verify { postsRepository.getAllByPostType(MockPostType.QUESTION, any()) }
    }
}
