package com.trivialepic.mockexchange.posts

import com.trivialepic.mockexchange.objects.posts.MockPost
import com.trivialepic.mockexchange.objects.posts.MockPostType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.mediatype.hal.HalModelBuilder
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import org.springframework.web.reactive.function.server.ServerResponse.created
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

@Component
class PostsApiHandler(private val postsRepository: PostsRepository) {

    @Autowired
    lateinit var xabcRepository: XabcRepository

    companion object {

        @JvmStatic
        private val logger = LoggerFactory.getLogger(PostsApiHandler::class.java)

        private fun requestToPageable(request: ServerRequest): Pageable {
            val limit = request.queryParam("size").map(String::toInt).filter { it > 0 }.orElse(20)
            val page = request.queryParam("page").map(String::toInt).orElse(0)
            val offset = page * limit
            val sortBy = request.queryParam("sortBy").orElse("id")
            val sortDir = request.queryParam("sortDir").map(Direction::valueOf).orElse(Direction.ASC)
            return PageRequest.of(offset / limit, limit, sortDir, sortBy)
        }
    }

    fun getQuestions(request: ServerRequest): Mono<ServerResponse> {
        val pageable = requestToPageable(request)
        val questions = postsRepository.findQuestions(pageable.offset, pageable.pageSize).collectList()
        val count = postsRepository.countByPostType(MockPostType.QUESTION)
        return Mono.zip(questions, count) { q, c -> PageableExecutionUtils.getPage(q, pageable) { c } }
            .map {
                PagedModel.of(it.content, pageableToMetadata(it.pageable, it.totalElements, it.totalPages))
            }
            .flatMap { ok().contentType(MediaType.APPLICATION_JSON).bodyValue(it) }
    }

    fun getQuestion(request: ServerRequest): Mono<ServerResponse> {
        val q = postsRepository.findQuestion(request.pathVariable("id").toLong())
        return q.flatMap { ok().contentType(MediaType.APPLICATION_JSON).bodyValue(it) }
    }

    fun getAnswers(request: ServerRequest): Mono<ServerResponse> {
        return Mono.justOrEmpty(request.queryParam("parentId")).map(String::toLong)
            .flatMap { postsRepository.findAnswers(it).collectList() }
            .flatMap { ok().contentType(MediaType.APPLICATION_JSON).bodyValue(it) }
            .switchIfEmpty(badRequest().build())
    }

    fun createQuestion(request: ServerRequest): Mono<ServerResponse> {
//        return request.bodyToMono<QuestionRequest>()
//            .flatMap {xabcRepository.save(Xabc(title = it.title, body = it.body)) }
//            .flatMap { ok().bodyValue(it) }
        return request.bodyToMono<QuestionRequest>().map { a ->
                MockPost().apply {
                    title = a.title
                    body = a.body
                    postTypeId = MockPostType.QUESTION.ordinal
                    postType = MockPostType.QUESTION
                    contentLicense = "UNLICENSED"
                    ownerDisplayName = "x"
                }
            }
            .flatMap { postsRepository.save(it) }
            .flatMap {
                val uri = request.uriBuilder()
                    .path("/{id}")
                    .build(it.id)
                created(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(it)
            }
    }

    fun pageableToMetadata(pageable: Pageable, total: Long, totalPages: Int): PagedModel.PageMetadata {
        return PagedModel.PageMetadata(pageable.pageSize.toLong(), pageable.pageNumber.toLong(), total, totalPages.toLong())
    }
}
