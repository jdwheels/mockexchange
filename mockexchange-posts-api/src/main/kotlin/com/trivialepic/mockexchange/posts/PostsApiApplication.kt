package com.trivialepic.mockexchange.posts

import com.trivialepic.mockexchange.objects.MockEntity
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan(basePackageClasses = [MockEntity::class])
class PostsApiApplication

fun main(args: Array<String>) {
    runApplication<PostsApiApplication>(*args)
}
