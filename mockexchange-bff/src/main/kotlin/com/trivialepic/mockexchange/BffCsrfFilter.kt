package com.trivialepic.mockexchange

import org.springframework.security.web.server.csrf.CsrfToken
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class BffCsrfFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val key = CsrfToken::class.java.name
        val csrfToken: Mono<CsrfToken> = exchange.getAttribute(key) ?: Mono.empty()
        return csrfToken.then(chain.filter(exchange))
    }
}
