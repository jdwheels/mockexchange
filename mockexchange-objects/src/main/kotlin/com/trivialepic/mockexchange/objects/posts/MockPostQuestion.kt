package com.trivialepic.mockexchange.objects.posts

import org.springframework.data.rest.core.config.Projection
import java.time.Instant

@Projection(name = "question", types = [MockPost::class])
interface MockPostQuestion {
    var id: Long?
    var title: String?
}

interface MockUserSummary {
    var id: Long
    var displayName: String?
    var reputation: Long?
}

@Projection(name = "x", types = [MockPost::class])
interface MockPostQuestion2 {
    val id: Long
    val title: String?
    var postType: MockPostType
    var score: Long?
    var answerCount: Long?
    var viewCount: Long?
    var tags: Set<String>?
    var creationDate: Instant?
    var lastEditDate: Instant?
    var lastActivityDate: Instant?
    var closedDate: Instant?
    var communityOwnedDate: Instant?
    val ownerUser: MockUserSummary
    var lastEditorUser: MockUserSummary?
}

