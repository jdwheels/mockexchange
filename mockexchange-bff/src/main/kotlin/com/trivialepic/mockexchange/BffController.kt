package com.trivialepic.mockexchange

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.WebSession
import reactor.core.publisher.Flux

@RestController
class BffController {

    data class X(val y: String)

    @GetMapping
    fun x(s: WebSession): Flux<X> {
        s.attributes["x"] = "y"
        return s.save().thenMany(Flux.just(X("a"), X("b"), X("c")))
    }
}
