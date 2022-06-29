//
//import com.trivialepic.mockexchange.objects.posts.PostSummary
//import kotlinx.coroutines.MainScope
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.cache.Cache
//import org.springframework.cache.CacheManager
//import org.springframework.cache.support.SimpleValueWrapper
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
//import org.springframework.data.redis.core.ReactiveRedisOperations
//import org.springframework.data.redis.core.ReactiveRedisTemplate
//import org.springframework.data.redis.core.ReactiveValueOperations
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
//import org.springframework.data.redis.serializer.RedisSerializationContext
//import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder
//import org.springframework.data.redis.serializer.StringRedisSerializer
//import org.springframework.stereotype.Component
//import reactor.core.publisher.Mono
//import java.util.concurrent.Callable
//
//
//@SpringBootTest
//class BffCacheTests {
//
//
//
//    @Configuration
//    class X {
//        @Bean
//        fun reactiveRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, PostSummary> {
//            val keySerializer = StringRedisSerializer()
//            val valueSerializer: Jackson2JsonRedisSerializer<PostSummary> =
//                Jackson2JsonRedisSerializer(PostSummary::class.java)
//            val builder: RedisSerializationContextBuilder<String, PostSummary> =
//                RedisSerializationContext.newSerializationContext(keySerializer)
//            val context: RedisSerializationContext<String, PostSummary> = builder.value(valueSerializer).build()
//            return ReactiveRedisTemplate(factory, context)
//        }
//    }
//
//
//    interface ReactiveCacheManager: CacheManager {
//        override fun getCache(name: String): Mono<ReactiveCache>
//
//        override fun getCacheNames(): Mono<MutableCollection<String>>
//    }
//
//    interface ReactiveCache: Cache {
//        override fun getName(): Mono<String>
//
//        override fun getNativeCache(): Mono<Any>
//
//        override fun get(key: Any): Mono<Cache.ValueWrapper>
//
//        override fun <T : Any?> get(key: Any, type: Class<T>?): Mono<T?>
//
//        override fun <T : Any?> get(key: Any, valueLoader: Callable<T>): Mono<T?>
//
//        override fun put(key: Any, value: Any?): Mono<Void>
//
//        override fun evict(key: Any): Mono<Void>
//
//        override fun clear(): Mono<Void>
//
//    }
//
//    @Component
//    class XC(
//        val redisOps: ReactiveRedisOperations<String, PostSummary>,
//        val redisTemplate: ReactiveRedisTemplate<String, PostSummary>,
//        val redisValueOpts: ReactiveValueOperations<String, PostSummary>
//        ) : ReactiveCache {
//
//        val scope = MainScope()
//        override fun getName(): Mono<String> {
//            TODO("Not yet implemented")
//        }
//
//        override fun getNativeCache(): Mono<Any> {
//            TODO("Not yet implemented")
//        }
//
//        override fun get(key: Any): Mono<Cache.ValueWrapper> {
//            return redisValueOpts.get(key).map { SimpleValueWrapper(it) }
//            TODO("Not yet implemented")
//        }
//
//        override fun <T> get(key: Any, type: Class<T>?): Mono<T?> {
//            return redisValueOpts.get(key).map { it as T }
//            redisOps.opsForList().
//            TODO("Not yet implemented")
//        }
//
//        override fun <T> get(key: Any, valueLoader: Callable<T>): Mono<T?> {
//            TODO("Not yet implemented")
//        }
//
//        override fun put(key: Any, value: Any?): Mono<Void> {
//            TODO("Not yet implemented")
//        }
//
//        override fun evict(key: Any): Mono<Void> {
//            TODO("Not yet implemented")
//        }
//
//        override fun clear(): Mono<Void> {
//            TODO("Not yet implemented")
//        }
//
//
//    }
//
//}
