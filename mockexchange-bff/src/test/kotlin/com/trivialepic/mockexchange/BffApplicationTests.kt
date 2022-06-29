package com.trivialepic.mockexchange

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(properties = ["mockexchange-bff.user-api.base-url=https://localhost"])
@ActiveProfiles("test")
class BffApplicationTests {

    @Test
    fun contextLoads() {}
}
