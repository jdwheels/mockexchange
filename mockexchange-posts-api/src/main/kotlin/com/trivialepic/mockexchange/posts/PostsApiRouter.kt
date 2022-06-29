package com.trivialepic.mockexchange.posts

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router

@Configuration
class PostsApiRouter(private val handler: PostsApiHandler) {

    @Bean
    fun postsRouterFunction(): RouterFunction<ServerResponse> {
        return router {
            accept(APPLICATION_JSON).nest {
                GET("/posts/questions", handler::getQuestions)
                POST("/posts/questions", handler::createQuestion)
                GET("/posts/questions/{id}", handler::getQuestion)
                GET("/posts/answers", handler::getAnswers)
            }
        }
    }
}
