package com.trivialepic.mockexchange.objects.posts

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import com.trivialepic.mockexchange.objects.MockEntity
import com.trivialepic.mockexchange.objects.users.MockUser
import org.apache.solr.client.solrj.beans.Field
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.FetchProfile
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.NotFound
import org.hibernate.annotations.NotFoundAction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.ConstraintMode
import javax.persistence.Convert
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.ForeignKey
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.Lob
import javax.persistence.ManyToOne
import javax.persistence.NamedAttributeNode
import javax.persistence.NamedEntityGraph
import javax.persistence.OneToMany
import javax.persistence.Version
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.PositiveOrZero
import javax.validation.constraints.Size

@Entity
@EntityListeners(AuditingEntityListener::class)
//@NamedEntityGraph(name = "MockPost.votes",
//    attributeNodes = [NamedAttributeNode("votes")])
class MockPost : MockEntity<Long> {

    @Field
    @XStreamAsAttribute
    @XStreamAlias("Id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mock_post_id_seq")
    @field:Min(-1)
    @GenericGenerator(
        name = "mock_post_id_seq",
        strategy = "com.trivialepic.mockexchange.objects.MockSequenceGenerator",
    )
    @Column(nullable = false)
    override var id: Long? = null

    @Field
    @XStreamAsAttribute
    @XStreamAlias("PostTypeId")
    @Enumerated(EnumType.ORDINAL)
    @field:NotNull
    @Column(name = "post_type_id", nullable = false)
    var postType: MockPostType? = null

    @Field
    @XStreamAsAttribute
    @XStreamAlias("ParentId")
    @field:PositiveOrZero
    @Column(name = "parent_id")
    var parentId: Long? = null

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_id", updatable = false, insertable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
//    var parent: MockPost? = null
//
//    @OneToMany
//    @JoinColumn(name = "parent_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
//    var children: Set<MockPost>? = null

    @Field
    @XStreamAsAttribute
    @XStreamAlias("AcceptedAnswerId")
    @Column(name = "accepted_answer_id")
    @field:PositiveOrZero
    var acceptedAnswerId:  Long? = null

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "accepted_answer_id", insertable = false, updatable = false, foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
//    var acceptedAnswer: MockPost? = null

    @Field
    @XStreamAsAttribute
    @XStreamAlias("OwnerUserId")
    @field:Min(-1)
    @Column(name = "owner_user_id")
    var ownerUserId: Long? = null

//    @Fetch(FetchMode.JOIN)
//    @ElementCollection
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//        name = "owner_user_id",
//        updatable = false,
//        insertable = false,
//        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT)
//    )

//    @Fetch(FetchMode.JOIN)
    @ManyToOne
//    @field:javax.persistence.Transient
//    @field:JsonSerialize
//    @field:JsonDeserialize

//    @JsonManagedReference
//    @JsonGetter(value = "x")
        @JoinColumn(
            name = "owner_user_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false,
            foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT
        ))
    @NotFound(action = NotFoundAction.IGNORE)
    var ownerUser: MockUser? = null

//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "postId", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT), updatable = false, insertable = false)
//    var votes: List<MockVote>? = null

    @XStreamAsAttribute
    @XStreamAlias("OwnerDisplayName")
    @field:Size(min = 1, max = 127)
    @Column(length = 127)
    var ownerDisplayName: String? = null

    @Field
    @XStreamAsAttribute
    @XStreamAlias("LastEditorUserId")
    @field:Min(-1)
    @Column
    var lastEditorUserId:  Long? = null

    @ManyToOne
    @JoinColumn(
        name = "lastEditorUserId",
        referencedColumnName = "id",
        updatable = false,
        insertable = false,
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    @NotFound(action = NotFoundAction.IGNORE)
    @Fetch(FetchMode.JOIN)
    var lastEditorUser: MockUser? = null

    @XStreamAsAttribute
    @XStreamAlias("LastEditorDisplayName")
    @field:Size(min = 1, max = 127)
    @Column(length = 127)
    var lastEditorDisplayName: String? = null

    @Field
    @XStreamAsAttribute
    @XStreamAlias("Score")
    @field:NotNull
    @Column(nullable = false)
    var score: Long = 0

    @Field
    @XStreamAsAttribute
    @XStreamAlias("Title")
    @field:Size(min = 1, max = 255)
    @Column
    var title: String? = null

    @XStreamAsAttribute
    @XStreamAlias("Body")
    @field:NotNull
    @Lob
    @Column(columnDefinition = "text", nullable = false)
    var body: String? = null

    @Field
    @XStreamAsAttribute
    @XStreamAlias("ContentLicense")
    @field:NotBlank @field:Size(min = 1, max = 127)
    @Column(length = 127, nullable = false)
    var contentLicense: String? = null

    @XStreamAsAttribute
    @XStreamAlias("Tags")
    @Column(name = "tags", length = 127)
    var allTags: @Size(min = 1, max = 127) String? = null

    @Field
    @Column(name = "tags", insertable = false, updatable = false)
    @Convert(converter = MockTagConverter::class)
    var tags: Set<String>? = null

    @XStreamAsAttribute
    @XStreamAlias("CreationDate")
    @CreatedDate
    @field:PastOrPresent
    @Column(nullable = false, updatable = false)
    var creationDate: LocalDateTime? = null

    @XStreamAsAttribute
    @XStreamAlias("LastEditDate")
    @field:PastOrPresent
    @Column
    var lastEditDate: LocalDateTime? = null

    @XStreamAsAttribute
    @XStreamAlias("LastActivityDate")
    @field:PastOrPresent
    @Column(nullable = false)
    @LastModifiedDate
    @Version
    var lastActivityDate: LocalDateTime? = null

    @XStreamAsAttribute
    @XStreamAlias("ClosedDate")
    @field:PastOrPresent
    @Column
    var closedDate: LocalDateTime? = null

    @XStreamAsAttribute
    @XStreamAlias("CommunityOwnedDate")
    @field:PastOrPresent
    @Column
    var communityOwnedDate: LocalDateTime? = null

    @XStreamAsAttribute
    @XStreamAlias("ViewCount")
    @field:PositiveOrZero
    @Column
    var viewCount: Long? = null

    @XStreamAsAttribute
    @XStreamAlias("CommentCount")
    @field:PositiveOrZero
    @Column
    var commentCount: Long? = null

    @Field
    @XStreamAsAttribute
    @XStreamAlias("AnswerCount")
    @field:PositiveOrZero
    @Column
    var answerCount: Long? = null

    @Field
    @XStreamAsAttribute
    @XStreamAlias("FavoriteCount")
    @field:PositiveOrZero
    @Column
    var favoriteCount: Long? = null
}
