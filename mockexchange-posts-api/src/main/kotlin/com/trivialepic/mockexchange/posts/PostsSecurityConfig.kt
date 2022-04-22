package com.trivialepic.mockexchange.posts

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.session.web.http.CookieSerializer
import org.springframework.session.web.http.DefaultCookieSerializer


@Configuration
class PostsSecurityConfig(private val postsApiProperties: PostsApiProperties) {

    @Bean
    fun cookieSerializer(): CookieSerializer {
        val serializer = DefaultCookieSerializer()

        if (postsApiProperties.cookieDomain != null) {
            serializer.setCookieName("SESSION_KEY")
            serializer.setDomainName(postsApiProperties.cookieDomain)
        }
//        serializer.setDomainName("domain.com")
//        serializer.setCookiePath("/")
//        serializer.setCookieMaxAge(10) //Set the cookie max age in seconds, e.g. 10 seconds
        return serializer
    }




    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authz ->
                authz
                    .antMatchers("/x/user/**", "/null/**").permitAll()
                    .antMatchers("/actuator/health/**").permitAll()
                    .anyRequest().authenticated().and()
                    .logout().logoutSuccessHandler(HttpStatusReturningLogoutSuccessHandler()).permitAll().and()
                    .csrf { c ->
                        val cookieRepo = CookieCsrfTokenRepository.withHttpOnlyFalse()
                        if (postsApiProperties.cookieDomain != null) {
                            cookieRepo.setCookieDomain(postsApiProperties.cookieDomain)
                        }
                        c.csrfTokenRepository(cookieRepo)
                    }
            }
            .exceptionHandling { e -> e.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))}
            .oauth2Login().defaultSuccessUrl(postsApiProperties.loginSuccessUrl ?: "/")
//            .httpBasic(withDefaults())
        return http.build()
    }
}
