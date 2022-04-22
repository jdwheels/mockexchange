package com.trivialepic.mockexchange.posts

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("posts-api")
class PostsApiProperties {

    var cookieDomain: String? = null

    var loginSuccessUrl: String? = null
}
