package com.trivialepic.mockexchange.objects.users

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.PastOrPresent
import javax.validation.constraints.Positive

@Entity
class MockBadge(

    @XStreamAsAttribute
    @XStreamAlias("Id")
    @Id
    @Column(nullable = false)
    @field:Positive
    val id: Long,

    @XStreamAsAttribute
    @XStreamAlias("UserId")
    @field:Min(-1)
    val userId: Long?,

    @XStreamAsAttribute
    @XStreamAlias("Name")
    @field:NotEmpty
    val name: String,

    @XStreamAsAttribute
    @XStreamAlias("Date")
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @field:PastOrPresent
    val date:  LocalDateTime,

    @XStreamAsAttribute
    @XStreamAlias("Class")
    @Column(nullable = false)
    @field:NotNull @field:Positive
    val classType: Int,

    @XStreamAsAttribute
    @XStreamAlias("TagBased")
    @Column(nullable = false)
    @field:NotNull
    val tagBased: Boolean
)
