package com.trivialepic.mockexchange.post

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.trivialepic.mockexchange.objects.posts.PostSummary
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.hateoas.PagedModel
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.client.bodyToFlux
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class PostService(
    private val factory: ReactiveRedisConnectionFactory,
    private val postOps: ReactiveRedisOperations<String, PagedModel<PostSummary>>,
    private val postApiClient: WebClient, cacheManager: CacheManager,
    private val objectMapper: ObjectMapper
) {
    private val cache: Cache

    init {
        cache = cacheManager.getCache("posts")!!
    }

    private inline fun <reified T> typeReference() = object : TypeReference<T>() {}

    fun getQuestions(size: Long, page: Long): Mono<PagedModel<PostSummary>> {
        val key = cacheKey(size, page)
//        return
//        Mono.justOrEmpty<ValueWrapper>(
        return postOps.opsForValue().get(key)
//           return coffeeOps.hasKey(key).flatMap { coffeeOps.opsForValue().get(key) }
//            cache.get(cacheKey(size, page))
//        )
//            .map {
//                objectMapper.convertValue(it.get(), typeReference<PagedModel<PostSummary>>())
//            }
            .switchIfEmpty(
                fetchQuestions(size, page).doOnNext { postOps.opsForValue().set(key, it).subscribe() }
//                    .doOnNext { cache.put(cacheKey(size, page), it) }
            )
    }

    fun getQuestion(id: Long): Mono<PostSummary> {
        return postApiClient.get().uri("/posts/questions/{id}", id)
            .retrieve()
            .bodyToMono()
    }

    fun getAnswers(parentId: Long): Flux<PostSummary> {
        return postApiClient.get()
            .uri {
                it.path("/posts/answers").queryParam("parentId", parentId).build()
            }
            .retrieve()
            .bodyToFlux()
    }

    fun clear(): Mono<Unit> {
        return Mono.fromRunnable<Unit>(cache::clear).subscribeOn(Schedulers.boundedElastic())
    }

    private fun fetchQuestions(size: Long, page: Long): Mono<PagedModel<PostSummary>> {
        return postApiClient.get()
            .uri {
                it.path("/posts/questions")
                    .queryParam("size", size)
                    .queryParam("page", page)
                    .build()
            }
            .retrieve()
            .bodyToMono<PagedModel<PostSummary>>()
    }


    private fun cacheKey(size: Long, page: Long): String {
//        return UUID.randomUUID().toString()
        return "getQuestions($size, $page)"
    }
}
