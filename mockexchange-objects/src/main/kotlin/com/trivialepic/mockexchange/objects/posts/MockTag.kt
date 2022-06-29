package com.trivialepic.mockexchange.objects.posts

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Entity
class MockTag(

    @XStreamAsAttribute
    @XStreamAlias("Id")
    @Id
    @Column(nullable = false)
    @field:Positive
    val id: Long,

    @XStreamAsAttribute
    @XStreamAlias("TagName")
    @Column(nullable = false)
    @field:NotEmpty
    val tagName: String,

    @XStreamAsAttribute
    @XStreamAlias("Count")
    @Column(nullable = false)
    @field:NotNull
    val count: Long,

    @XStreamAsAttribute
    @XStreamAlias("ExcerptPostId")
    @Column
    @field:Min(-1)
    val excerptPostId:  Long,

    @XStreamAsAttribute
    @XStreamAlias("ExcerptPostId")
    @Column
    @field:Min(-1)
    val wikiPostId: Long,
)
