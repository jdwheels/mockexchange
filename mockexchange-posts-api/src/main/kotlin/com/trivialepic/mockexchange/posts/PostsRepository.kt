package com.trivialepic.mockexchange.posts

import com.trivialepic.mockexchange.objects.posts.MockPost
import com.trivialepic.mockexchange.objects.posts.MockPostQuestion
import com.trivialepic.mockexchange.objects.posts.MockPostQuestion2
import com.trivialepic.mockexchange.objects.posts.MockPostType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.graphql.data.GraphQlRepository
import javax.persistence.criteria.Predicate

@GraphQlRepository
//@RepositoryRestResource(excerptProjection = MockPostQuestion2::class)
@RepositoryRestResource
interface PostsRepository : JpaRepository<MockPost, Long>, QuerydslPredicateExecutor<MockPost> {
    //
    fun findAllByPostType(postType: MockPostType, pageable: Pageable): Page<MockPostQuestion2>

    //
    fun findAllBy(predicate: Predicate, pageable: Pageable): Page<MockPostQuestion2>

    @Query(
        value = "select post FROM MockPost post " +
                "left outer join fetch post.ownerUser ownerUser " +
                "left outer join fetch post.lastEditorUser lastEditorUser " +
                "where post.postType = :postType",
        countQuery = "select count(post) FROM MockPost post " +
                " where post.postType = :postType"
    )
    fun x(@Param("postType") postType: MockPostType, pageable: Pageable): Page<MockPostQuestion2>

    @Query(
        value = "select post FROM MockPost post " +
                "left outer join fetch post.ownerUser ownerUser " +
                "left outer join fetch post.lastEditorUser lastEditorUser " +
                "where post.postType = com.trivialepic.mockexchange.objects.posts.MockPostType.ANSWER " +
                "and post.parentId = :parentId " +
                "order by post.score desc ",
        countQuery = "select count(post) FROM MockPost post " +
                "where post.postType = com.trivialepic.mockexchange.objects.posts.MockPostType.ANSWER " +
                "and post.parentId = :parentId"
    )
    fun xy(@Param("parentId") parentId: Long, pageable: Pageable): Page<MockPost>
}
