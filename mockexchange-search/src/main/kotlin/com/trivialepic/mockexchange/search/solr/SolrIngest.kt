package com.trivialepic.mockexchange.search.solr

import com.trivialepic.mockexchange.objects.posts.MockPostType
import com.trivialepic.mockexchange.search.PostsRepository
import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.client.solrj.response.UpdateResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import kotlin.streams.toList


@Component
class SolrIngest(private val solrClient: SolrClient,
                 private val postsRepository: PostsRepository) : InitializingBean {

    companion object {
        @Suppress("JAVA_CLASS_ON_COMPANION")
        @JvmStatic
        private val logger = LoggerFactory.getLogger(javaClass.enclosingClass)

        @JvmStatic
        private val COLLECTION_NAME: String = "posts"
    }

    fun x() {
        var page = Pageable.ofSize(100)

        do {
            val posts = postsRepository.getAllByPostType(MockPostType.QUESTION, page)
            logger.info("${page.pageNumber}, ${posts.totalPages}")
            solrClient.addBeans(COLLECTION_NAME, posts.get().toList())
            page = posts.pageable.next()
            solrClient.commit(COLLECTION_NAME)
        } while (posts.hasNext())
    }

    override fun afterPropertiesSet() {
        logger.info("x")
        x()
    }
}
