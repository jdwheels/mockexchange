package com.trivialepic.mockexchange.objects.comments

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import com.trivialepic.mockexchange.objects.users.MockUser
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import org.hibernate.annotations.Type
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.ConstraintMode
import javax.persistence.Entity
import javax.persistence.ForeignKey
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.Lob
import javax.persistence.ManyToOne
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

@Entity
class MockComment(

    @XStreamAsAttribute
    @XStreamAlias("Id")
    @Id
    @Column(nullable = false)
    @field:Min(-1)
    val id: Long,

    @XStreamAsAttribute
    @XStreamAlias("PostId")
    @field:NotNull @field:PositiveOrZero
    val postId: Long,

    @XStreamAsAttribute
    @XStreamAlias("Score")
    @Column(nullable = false)
    @field:NotNull
    val score: Long,

    @XStreamAsAttribute
    @XStreamAlias("Text")
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @field:NotNull @field:Size(max = 600)
    val text: String,

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

    @ManyToOne
//    @field:javax.persistence.Transient
//    @field:JsonSerialize
//    @field:JsonDeserialize

//    @JsonManagedReference
//    @JsonGetter(value = "x")
    @JoinColumn(
        name = "userId",
        insertable = false,
        updatable = false,
        foreignKey = ForeignKey(
            ConstraintMode.NO_CONSTRAINT
        )
    )
    @NotFound(action = NotFoundAction.IGNORE)
    var user: MockUser? = null,


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "owner_user_id", updatable = false, insertable = false)
//    val ownerUser: MockUser?,

    @XStreamAsAttribute
    @XStreamAlias("UserDisplayName")
    @Column(length = 127)
    @field:Size(min = 1, max = 127)
    val userDisplayName: String?,

    @XStreamAsAttribute
    @XStreamAlias("ContentLicense")
    @Column(length = 127, nullable = false)
    @field:NotBlank @field:Size(min = 1, max = 127)
    val contentLicense: String,
)
