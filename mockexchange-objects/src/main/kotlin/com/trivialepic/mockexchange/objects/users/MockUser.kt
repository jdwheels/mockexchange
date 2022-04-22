package com.trivialepic.mockexchange.objects.users

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import com.trivialepic.mockexchange.objects.posts.MockPost
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Pattern
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

@Entity
class MockUser(

    @XStreamAsAttribute
    @XStreamAlias("Id")
    @Id
    @Column(nullable = false)
    @field:Min(-1)
    val id: Long,

    @XStreamAsAttribute
    @XStreamAlias("Reputation")
    @Column(columnDefinition = "bigint default 0", nullable = false)
    @field:NotNull
    val reputation: Long,

    @XStreamAsAttribute
    @XStreamAlias("CreationDate")
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @field:PastOrPresent
    val creationDate: LocalDateTime,

    @XStreamAsAttribute
    @XStreamAlias("DisplayName")
    @Column(length = 63, nullable = false)
    @field:NotBlank @field:Size(min = 1, max = 63)
    val displayName: String,

    @XStreamAsAttribute
    @XStreamAlias("LastAccessDate")
    @LastModifiedDate
    @Column(nullable = false)
    @field:PastOrPresent
    val lastAccessDate: LocalDateTime,

    @XStreamAsAttribute
    @XStreamAlias("WebsiteUrl")
    @Column
    @field:Size(max = 255) @field:Pattern(regexp = "(?i)^(https?://.*)|$")
    val websiteUrl: String?,

    @XStreamAsAttribute
    @XStreamAlias("Location")
    @Column(length = 127)
    @field:Size(max = 127)
    val location: String?,

    @XStreamAsAttribute
    @XStreamAlias("AboutMe")
    @Column(length = 8191)
    @field:Size(max = 8191)
    val aboutMe: String?,

    @XStreamAsAttribute
    @XStreamAlias("Views")
    @Column(columnDefinition = "bigint default 0", nullable = false)
    @field:NotNull @field:PositiveOrZero
    val views: Long,

    @XStreamAsAttribute
    @XStreamAlias("UpVotes")
    @Column(columnDefinition = "bigint default 0", nullable = false)
    @field:NotNull @field:PositiveOrZero
    val upVotes: Long,

    @XStreamAsAttribute
    @XStreamAlias("DownVotes")
    @Column(columnDefinition = "bigint default 0", nullable = false)
    @field:NotNull @field:PositiveOrZero
    val downVotes: Long,

    @XStreamAsAttribute
    @XStreamAlias("AccountId")
    @Column
    val accountId: Long?,

    @XStreamAsAttribute
    @XStreamAlias("ProfileImageUrl")
    @Column
    @field:Size(max = 255) @field:Pattern(regexp = "(?i)^(https?://.*)|$")
    val profileImageUrl: String?,
//
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "owner_user_id")
////    @JsonBackReference
//    val posts: Set<MockPost>?,
)
