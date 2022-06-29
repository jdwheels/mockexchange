package com.trivialepic.mockexchange.search

import com.trivialepic.mockexchange.objects.posts.MockPost
import com.trivialepic.mockexchange.objects.posts.MockPostType
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostsRepository : JpaRepository<MockPost, Long> {
    fun getAllByPostType(postType: MockPostType, pageable: Pageable): Page<MockPost>
}
