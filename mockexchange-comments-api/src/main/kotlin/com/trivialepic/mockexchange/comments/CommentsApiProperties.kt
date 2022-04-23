package com.trivialepic.mockexchange.comments

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("comments-api")
class CommentsApiProperties {

    var cookieDomain: String? = null

    var loginSuccessUrl: String? = null
}
