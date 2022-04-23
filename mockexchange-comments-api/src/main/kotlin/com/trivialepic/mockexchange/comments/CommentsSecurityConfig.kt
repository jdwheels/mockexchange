package com.trivialepic.mockexchange.comments

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
class CommentsSecurityConfig(private val commentsApiProperties: CommentsApiProperties) {

    @Bean
    fun cookieSerializer(): CookieSerializer {
        val serializer = DefaultCookieSerializer()

        if (commentsApiProperties.cookieDomain != null) {
            serializer.setCookieName("SESSION_KEY")
            serializer.setDomainName(commentsApiProperties.cookieDomain)
        }
        return serializer
    }




    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authz ->
                authz
                    .antMatchers("/actuator/health/**").permitAll()
                    .anyRequest().authenticated().and()
//                    .logout().logoutSuccessHandler(HttpStatusReturningLogoutSuccessHandler()).permitAll().and()
//                    .csrf { c ->
//                        val cookieRepo = CookieCsrfTokenRepository.withHttpOnlyFalse()
//                        if (commentsApiProperties.cookieDomain != null) {
//                            cookieRepo.setCookieDomain(commentsApiProperties.cookieDomain)
//                        }
//                        c.csrfTokenRepository(cookieRepo)
//                    }
            }
            .exceptionHandling { e -> e.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))}
//            .oauth2Login().defaultSuccessUrl(commentsApiProperties.loginSuccessUrl ?: "/")
//            .httpBasic(withDefaults())
        return http.build()
    }
}
