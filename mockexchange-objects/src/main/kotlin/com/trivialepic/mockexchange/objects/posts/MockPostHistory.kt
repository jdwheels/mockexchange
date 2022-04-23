package com.trivialepic.mockexchange.objects.posts

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import org.hibernate.annotations.Type
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

@Entity
class MockPostHistory(
    @XStreamAsAttribute
    @XStreamAlias("Id")
    @Id
    @Column(nullable = false)
    @field:Positive
    val id:  Long,

    @XStreamAsAttribute
    @XStreamAlias("PostHistoryTypeId")
    @Column(nullable = false)
    @field:NotNull @field:Positive
    val postHistoryTypeId:  Int,

    @XStreamAsAttribute
    @XStreamAlias("PostId")
    @Column(nullable = false)
    @field:NotNull @field:PositiveOrZero
    val postId: Long,

    @XStreamAsAttribute
    @XStreamAlias("RevisionGUID")
    @Column(nullable = false)
    val revisionGuid: UUID,

    @XStreamAsAttribute
    @XStreamAlias("CreationDate")
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @field:PastOrPresent
    val creationDate: LocalDateTime,

    @XStreamAsAttribute
    @XStreamAlias("UserId")
    @field:Min(-1)
    val userId: Long?,

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "owner_user_id", updatable = false, insertable = false)
//    val ownerUser: MockUser?,

    @XStreamAsAttribute
    @XStreamAlias("UserDisplayName")
    @Column(length = 127)
    @field:Size(min = 1, max = 127)
    val userDisplayName: String?,


    @XStreamAsAttribute
    @XStreamAlias("Text")
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    val text: String,

    @XStreamAsAttribute
    @XStreamAlias("ContentLicense")
    @Column(length = 127)
    @field:Size(min = 1, max = 127)
    val contentLicense:  String?,
)
