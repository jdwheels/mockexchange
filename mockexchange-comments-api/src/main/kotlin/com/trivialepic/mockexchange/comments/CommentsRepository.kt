package com.trivialepic.mockexchange.comments

import com.trivialepic.mockexchange.objects.comments.MockComment
import com.trivialepic.mockexchange.objects.posts.MockPostQuestion2
import com.trivialepic.mockexchange.objects.posts.MockPostType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@Repository
@RepositoryRestResource
interface CommentsRepository : JpaRepository<MockComment, Long> {

    @Query(value = "select comment from MockComment comment " +
            "left outer join fetch comment.user ownerUser " +
            "where comment.postId in (:postIds) " +
        "order by comment.postId, comment.creationDate")
    fun getCommentsFor(@Param("postIds") postIds: Set<Long>): List<MockComment>
}
