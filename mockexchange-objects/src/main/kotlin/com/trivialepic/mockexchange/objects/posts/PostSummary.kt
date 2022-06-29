package com.trivialepic.mockexchange.objects.posts

import java.time.LocalDateTime

data class PostSummary(
        val title: String? = null,
        val id: Long? = null,
        val body: String? = null,
        val answerCount: Long? = null,
        val votes: Long? = null,
        val score: Long? = null,
        val lastEditDate: LocalDateTime? = null,
        val lastEditorUserId: Long? = null,
        val lastEditorReputation: Long? = null,
        val lastEditorDisplayName: String? = null,
    )
