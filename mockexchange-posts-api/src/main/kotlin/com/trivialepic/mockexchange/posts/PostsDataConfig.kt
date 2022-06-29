package com.trivialepic.mockexchange.posts

import com.trivialepic.mockexchange.objects.posts.MockPost
import com.trivialepic.mockexchange.objects.posts.MockPostType
import io.r2dbc.postgresql.codec.EnumCodec
import io.r2dbc.spi.Option
import org.springframework.boot.autoconfigure.r2dbc.ConnectionFactoryOptionsBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.convert.CustomConversions
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.convert.EnumWriteSupport
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.DialectResolver
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback
import org.springframework.r2dbc.core.DatabaseClient
import reactor.core.publisher.Mono


@Configuration
@EnableR2dbcAuditing
class PostsDataConfig {

//  @Bean
//  fun connectionFactoryOptionsBuilderCustomizer(): ConnectionFactoryOptionsBuilderCustomizer? {
//    return ConnectionFactoryOptionsBuilderCustomizer { builder ->
//      builder.option(
//        Option.valueOf("extensions"),
//        listOf(
//          EnumCodec.builder()
//            .withEnum("post_type", MockPostType::class.java)
//            .withRegistrationPriority(EnumCodec.Builder.RegistrationPriority.FIRST)
//            .build()
//        )
//      )
//    }
//  }

//  class PostTypeWritingConverter : EnumWriteSupport<MockPostType>()

//  @Bean
//  fun r2dbcCustomConversions(databaseClient: DatabaseClient): R2dbcCustomConversions? {
//    val dialect = DialectResolver.getDialect(databaseClient.connectionFactory)
//    val converters = ArrayList(dialect.converters)
//    converters.addAll(R2dbcCustomConversions.STORE_CONVERTERS)
////    return R2dbcCustomConversions(
////      CustomConversions.StoreConversions.of(dialect.simpleTypeHolder, converters),
////      listOf(PostTypeWritingConverter())
////    )
//  }

  @Bean
  fun idGeneratingCallback(databaseClient: DatabaseClient): BeforeConvertCallback<MockPost>? {
    return BeforeConvertCallback<MockPost> { post, _ ->
      if (post.id == null) {
        databaseClient.sql("SELECT nextval('mock_post_id_seq')")
          .map { row -> row.get(0, Number::class.java) }
          .first()
          .map {
            post.id = it!!.toLong()
            post
          }
      } else {
        Mono.just(post)
      }
    }
  }
}
