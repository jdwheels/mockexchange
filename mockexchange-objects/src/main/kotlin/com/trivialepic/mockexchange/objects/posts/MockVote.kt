package com.trivialepic.mockexchange.objects.posts

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Positive

@Entity
class MockVote(

    @XStreamAsAttribute
    @XStreamAlias("Id")
    @Id
    @Column(nullable = false)
    @field:Positive
    val id:  Long,

    @XStreamAsAttribute
    @XStreamAlias("PostId")
    @Column(nullable = false)
    @field:NotNull @Min(-1)
    val postId: Long,

    @XStreamAsAttribute
    @XStreamAlias("VoteTypeId")
    @Column(nullable = false)
    @field:NotNull @Positive
    val voteTypeId: Int,

    @XStreamAsAttribute
    @XStreamAlias("UserId")
    @field:Min(-1)
    val userId: Long?,

    @XStreamAsAttribute
    @XStreamAlias("CreationDate")
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @field:PastOrPresent
    val creationDate: LocalDateTime,
)
