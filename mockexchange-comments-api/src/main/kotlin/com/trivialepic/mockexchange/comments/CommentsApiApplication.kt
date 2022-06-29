package com.trivialepic.mockexchange.comments

import com.trivialepic.mockexchange.objects.MockEntity
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan(basePackageClasses = [MockEntity::class])
class CommentsApiApplication

fun main(args: Array<String>) {
    runApplication<CommentsApiApplication>(*args)
}
