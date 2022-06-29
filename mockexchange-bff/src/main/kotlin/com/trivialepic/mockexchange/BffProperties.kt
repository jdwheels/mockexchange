package com.trivialepic.mockexchange

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Validated
@ConstructorBinding
@ConfigurationProperties("bff-api")
data class BffProperties(
    val cookieDomain: String? = null,
    val loginSuccessUrl: String? = null,
    @get:NotBlank val postApiBaseUrl: String,
)
