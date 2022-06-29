//package com.trivialepic.mockexchange.posts
//
//import org.springframework.security.core.annotation.AuthenticationPrincipal
//import org.springframework.security.oauth2.core.user.OAuth2User
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//import reactor.core.publisher.Mono
//import java.util.*
//
//
//@RestController
//@RequestMapping("/auth")
//class AuthController {
//
//    @GetMapping("/user")
//    fun user(@AuthenticationPrincipal principal: OAuth2User?): Mono<Map<String, OAuth2User?>> {
//        return Mono.just(Collections.singletonMap("user", principal));
//    }
//}
