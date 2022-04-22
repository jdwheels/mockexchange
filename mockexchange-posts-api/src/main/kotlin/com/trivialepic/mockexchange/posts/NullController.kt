package com.trivialepic.mockexchange.posts

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/null")
class NullController {

    @GetMapping
    fun n(): Unit? {
        return null
    }
}
