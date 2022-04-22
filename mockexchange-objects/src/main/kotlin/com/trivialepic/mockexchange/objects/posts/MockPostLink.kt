package com.trivialepic.mockexchange.objects.posts

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@Entity
class MockPostLink(
    @XStreamAsAttribute
    @XStreamAlias("Id")
    @Id
    @Column(nullable = false)
    @field:Positive
    val id: Long,

    @XStreamAsAttribute
    @XStreamAlias("CreationDate")
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @field:PastOrPresent
    val creationDate: LocalDateTime,

    @XStreamAsAttribute
    @XStreamAlias("PostId")
    @field:NotNull @field:PositiveOrZero
    val postId: Long,

    @XStreamAsAttribute
    @XStreamAlias("RelatedPostId")
    @field:NotNull @field:PositiveOrZero
    val relatedPostId:  Long,

    @XStreamAsAttribute
    @XStreamAlias("LinkTypeId")
    @Column(nullable = false)
    @field:NotNull @field:Positive
    val linkTypeId: Int,
)
