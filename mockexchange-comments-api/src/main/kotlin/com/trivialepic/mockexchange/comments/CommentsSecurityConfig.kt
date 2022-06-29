package com.trivialepic.mockexchange.comments

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.session.web.http.DefaultCookieSerializer
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
class CommentsSecurityConfig(private val commentsApiProperties: CommentsApiProperties) {

    @Bean
    fun cookieSerializer() = DefaultCookieSerializer().apply {
        setCookiePath("/")
        setUseBase64Encoding(false)
        if (commentsApiProperties.cookieDomain != null) {
            setDomainName(commentsApiProperties.cookieDomain)
        }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .authorizeHttpRequests { authz ->
            authz
                .antMatchers("/actuator/health/**").permitAll()
                .anyRequest().authenticated().and()
        }
        .cors().configurationSource(corsConfigurationSource()).and()
        .exceptionHandling { e -> e.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))}
        .build()

    private fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration().apply {
            if (commentsApiProperties.cookieDomain != null) {
                allowedOriginPatterns = listOf("https://*.${commentsApiProperties.cookieDomain}:[*]")
            }
            allowCredentials = true
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", corsConfiguration.applyPermitDefaultValues())
        }
    }
}
