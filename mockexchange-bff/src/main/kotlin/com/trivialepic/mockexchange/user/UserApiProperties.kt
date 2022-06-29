package com.trivialepic.mockexchange.user

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("mockexchange-bff.user-api")
data class UserApiProperties(var baseUrl: String)
