package com.trivialepic.mockexchange.posts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PostsApiApplication

fun main(args: Array<String>) {
    runApplication<PostsApiApplication>(*args)
}
