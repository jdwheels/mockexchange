package com.trivialepic.mockexchange.post

import com.trivialepic.mockexchange.objects.posts.PostSummary
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.hateoas.PagedModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/posts")
class PostController(private val postService: PostService) {

    @GetMapping("/questions")
    fun getQuestions(
        @RequestParam(defaultValue = "20") size: Long,
        @RequestParam(defaultValue = "0") page: Long,
    ): Mono<ResponseEntity<PagedModel<PostSummary>>> {
        return postService.getQuestions(size, page).map { ResponseEntity.ok().body(it) };
    }

    @GetMapping("/questions/{id}")
    fun getQuestion(@PathVariable id: Long): Mono<PostSummary> {
        return postService.getQuestion(id);
    }

    @GetMapping("/answers")
    fun getAnswers(@RequestParam parentId: Long): Flux<PostSummary> {
        return postService.getAnswers(parentId)
    }

    @GetMapping("/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun clear(): Mono<Void> {
        return postService.clear().then()
    }
}
