package com.trivialepic.mockexchange.posts

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Table("xabc")
data class Xabc(@Id var id: Int? = null, val title: String, val body: String)

@Repository
interface XabcRepository : ReactiveCrudRepository<Xabc, Int> {
}
