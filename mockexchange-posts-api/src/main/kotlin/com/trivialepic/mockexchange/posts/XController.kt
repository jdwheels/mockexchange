package com.trivialepic.mockexchange.posts

import com.trivialepic.mockexchange.objects.posts.MockPost
import com.trivialepic.mockexchange.objects.posts.MockPostQuestion2
import com.trivialepic.mockexchange.objects.posts.MockPostType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort.Direction
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping("/x")
class XController(private val postsRepository: PostsRepository) {

    @GetMapping("/pageable")
    fun pageable(): Pageable {
        return Pageable.ofSize(20).withPage(0)
    }

    @GetMapping("/user")
    fun user(@AuthenticationPrincipal principal: OAuth2User? = null): Map<String, Any?> {
        return Collections.singletonMap("user", principal)
    }

    @GetMapping("/page")
    fun page(): Page<MockPost> {
        return Page.empty()
    }

    @QueryMapping
    @GetMapping("/y")
    fun mockPosts(@Argument @RequestParam(required = false, defaultValue = "0") offset: Int? = 0,
                  @Argument @RequestParam(required = false, defaultValue = "20") limit: Int? = 20,
                  @Argument @RequestParam(required = false, defaultValue = "id") sortBy: String? = "id",
                  @Argument @RequestParam(required = false, defaultValue = "ASC") sortDir: Direction? = Direction.ASC): Page<MockPostQuestion2> {
        return postsRepository.x(MockPostType.QUESTION, PageRequest.of(offset!! / limit!!, limit, sortDir!!, sortBy))
    }
}
