package com.trivialepic.mockexchange.posts


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain


@Configuration
class PostsSecurityConfig(
//    private val postsApiProperties: PostsApiProperties
) {

//    @Bean
//    fun webSessionIdResolver(): WebSessionIdResolver? {
//        val resolver = CookieWebSessionIdResolver()
//        resolver.addCookieInitializer { it.path("/") }
//        if (postsApiProperties.cookieDomain != null) {
//            resolver.addCookieInitializer { it.domain(postsApiProperties.cookieDomain!!) }
//        }
//        return resolver
//    }

    @Bean
    fun  securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .authorizeExchange().anyExchange().permitAll()
            .and().csrf().disable()
//            .and()
//                .pathMatchers("/auth/user/**", "/actuator/health/**").permitAll()
//                .anyExchange().authenticated().and()
//            .logout().logoutSuccessHandler(HttpStatusReturningServerLogoutSuccessHandler()).and()
//            .csrf().csrfTokenRepository(csrfTokenRepository()).and()
//            .cors().configurationSource(corsConfigurationSource()).and()
//            .exceptionHandling().authenticationEntryPoint(HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)).and()
//            .oauth2Login().authenticationSuccessHandler(RedirectServerAuthenticationSuccessHandler(postsApiProperties.loginSuccessUrl ?: "/")).and()
            .build()
    }

//    private fun csrfTokenRepository(): ServerCsrfTokenRepository {
//        return CookieServerCsrfTokenRepository.withHttpOnlyFalse().apply {
//            setCookiePath("/")
//            setSecure(true)
//            if (postsApiProperties.cookieDomain != null) {
//                setCookieDomain(postsApiProperties.cookieDomain)
//            }
//        }
//    }
//
//    private fun corsConfigurationSource(): CorsConfigurationSource {
//        val corsConfiguration = CorsConfiguration().apply {
//            if (postsApiProperties.cookieDomain != null) {
//                allowedOriginPatterns = listOf("https://*.${postsApiProperties.cookieDomain}:[*]")
//            }
//            allowCredentials = true
//        }
//        return UrlBasedCorsConfigurationSource().apply {
//            registerCorsConfiguration("/**", corsConfiguration.applyPermitDefaultValues())
//        }
//    }
}
