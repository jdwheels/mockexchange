package com.trivialepic.mockexchange.github

import com.fasterxml.jackson.annotation.JsonValue

data class GithubEmail(
    val email: String,
    val primary: Boolean,
    val verified: Boolean,
    val visibility: Visibility?
) {
    enum class Visibility(@get:JsonValue val type: String) {
        PUBLIC("public"),
        PRIVATE("private")
    }
}
