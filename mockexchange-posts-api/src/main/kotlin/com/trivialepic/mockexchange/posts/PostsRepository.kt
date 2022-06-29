package com.trivialepic.mockexchange.posts

import com.trivialepic.mockexchange.objects.posts.MockPost
import com.trivialepic.mockexchange.objects.posts.MockPostType
import com.trivialepic.mockexchange.objects.posts.PostSummary
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Repository
interface PostsRepository : R2dbcRepository<MockPost, Long> {

    @Query("select * from mock_post_summary where post_type = 'QUESTION' order by id limit :limit offset :offset")
    fun findQuestions(offset: Long, limit: Int): Flux<PostSummary>

    @Query("select * from mock_post_summary where post_type_id = 1 and id = :id")
    fun findQuestion(id: Long): Mono<PostSummary>

    @Query("select * from mock_post_summary where post_type_id = 2 and parent_id = :parentId")
    fun findAnswers(parentId: Long): Flux<PostSummary>

    fun findByPostType(postType: MockPostType, pageable: Pageable): Flux<MockPost>

    fun countByPostType(postType: MockPostType): Mono<Long>

    fun findByPostTypeAndParentId(postType: MockPostType, parentId: Long): Flux<MockPost>
}
