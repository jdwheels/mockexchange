package com.trivialepic.mockexchange

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.trivialepic.mockexchange.objects.posts.PostSummary
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.hateoas.PagedModel
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession


@Configuration
@EnableRedisWebSession
class BffSessionConfig {

//    internal class JsonRedisSerializer : RedisSerializer<Any?> {
//        private val om: ObjectMapper
//
//        init {
//            om = ObjectMapper().enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY)
//        }
//
//        @Throws(SerializationException::class)
//        override fun serialize(t: Any?): ByteArray? {
//            return try {
//                om.writeValueAsBytes(t)
//            } catch (e: JsonProcessingException) {
//                throw SerializationException(e.getMessage(), e)
//            }
//        }
//
//        @Throws(SerializationException::class)
//        override fun deserialize(bytes: ByteArray?): Any? {
//            return if (bytes == null) {
//                null
//            } else try {
//                om.readValue(bytes, Any::class.java)
//            } catch (e: Exception) {
//                throw SerializationException(e.message, e)
//            }
//        }
//    }

    @Bean
    fun redisOperations(factory: ReactiveRedisConnectionFactory?, objectMapper: ObjectMapper): ReactiveRedisOperations<String, PagedModel<PostSummary>>? {
        val serializer: Jackson2JsonRedisSerializer<PagedModel<PostSummary>> = Jackson2JsonRedisSerializer(
            TypeFactory.defaultInstance().constructParametricType(
                PagedModel::class.java,
                PostSummary::class.java
            ))
        serializer.setObjectMapper(objectMapper)
        val builder: RedisSerializationContextBuilder<String, PagedModel<PostSummary>> =
            RedisSerializationContext.newSerializationContext(StringRedisSerializer())
        val context: RedisSerializationContext<String, PagedModel<PostSummary>> = builder.value(serializer).build()
        return ReactiveRedisTemplate(factory!!, context)
    }

//    @Bean
//    fun springSessionDefaultRedisSerializer(objectMapper: ObjectMapper): GenericJackson2JsonRedisSerializer {
//        return GenericJackson2JsonRedisSerializer(objectMapper)
//    }
//

    @Bean
    @Primary
    fun defaultCacheConfig(objectMapper: ObjectMapper): RedisCacheConfiguration? {

        return RedisCacheConfiguration.defaultCacheConfig()
//            .serializeKeysWith(
//                SerializationPair.fromSerializer(
//                    StringRedisSerializer()
//                )
//            )
            .serializeValuesWith(
                SerializationPair.fromSerializer<Any>(
                    GenericJackson2JsonRedisSerializer(objectMapper)
                )
            )
//            .prefixKeysWith("")
    }
}
