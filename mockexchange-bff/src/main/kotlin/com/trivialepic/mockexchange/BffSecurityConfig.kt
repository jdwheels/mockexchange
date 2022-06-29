package com.trivialepic.mockexchange

import com.trivialepic.mockexchange.github.GithubEmail
import com.trivialepic.mockexchange.github.GithubService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProvider
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.client.web.server.WebSessionServerOAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler
import org.springframework.security.web.server.authentication.logout.HttpStatusReturningServerLogoutSuccessHandler
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository
import org.springframework.security.web.server.csrf.ServerCsrfTokenRepository
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.server.session.CookieWebSessionIdResolver
import org.springframework.web.server.session.WebSessionIdResolver
import reactor.core.publisher.Mono
import java.util.*


@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties(BffProperties::class)
class BffSecurityConfig(private val bffProperties: BffProperties) {

    @Bean
    fun webSessionIdResolver(): WebSessionIdResolver {
        val resolver = CookieWebSessionIdResolver()
        resolver.addCookieInitializer { it.path("/") }
        if (bffProperties.cookieDomain != null) {
            resolver.addCookieInitializer { it.domain(bffProperties.cookieDomain!!) }
        }
        return resolver
    }

    @Bean
    fun authorizedClientRepository(): ServerOAuth2AuthorizedClientRepository {
        return WebSessionServerOAuth2AuthorizedClientRepository()
    }

    @Bean
    fun oauth2UserService(githubService: GithubService): ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {
        val delegate = DefaultReactiveOAuth2UserService()

        return ReactiveOAuth2UserService { userRequest ->
            val primaryEmail = githubService.getEmails(userRequest.accessToken)
                .filter(GithubEmail::verified)
                .skipUntil(GithubEmail::primary).take(1).single()
                .flatMap { Mono.just(it.email) }
            // Delegate to the default implementation for loading a user
            delegate.loadUser(userRequest)
                .zipWith(primaryEmail) { user, email ->
                    val map = user.attributes.toMutableMap()
                    map["email"] = email
                    DefaultOAuth2User(user.authorities, map, "email")
                }
        }
    }


    @Bean
    fun  securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .authorizeExchange()
            .pathMatchers("/user/**", "/actuator/health/**", "/posts/**").permitAll()
            .anyExchange().authenticated().and()
            .logout().logoutSuccessHandler(HttpStatusReturningServerLogoutSuccessHandler()).and()
            .csrf().csrfTokenRepository(csrfTokenRepository()).and()
            .cors().configurationSource(corsConfigurationSource()).and()
            .exceptionHandling()
                .authenticationEntryPoint(HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)).and()
            .oauth2Login {
                it.authorizedClientRepository(authorizedClientRepository())
                it.authenticationSuccessHandler(RedirectServerAuthenticationSuccessHandler(bffProperties.loginSuccessUrl ?: "/"))
            }
            .build()
    }

    private fun csrfTokenRepository(): ServerCsrfTokenRepository {
        return CookieServerCsrfTokenRepository.withHttpOnlyFalse().apply {
            setCookiePath("/")
            setSecure(true)
            if (bffProperties.cookieDomain != null) {
                setCookieDomain(bffProperties.cookieDomain)
            }
        }
    }

    private fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfiguration = CorsConfiguration().apply {
            if (bffProperties.cookieDomain != null) {
                allowedOriginPatterns = listOf("https://*.${bffProperties.cookieDomain}:[*]")
            }
            allowCredentials = true
        }
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", corsConfiguration.applyPermitDefaultValues())
        }
    }
}
