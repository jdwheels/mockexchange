package com.trivialepic.mockexchange.populator

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@AutoConfigureTestDatabase
class PopulatorApplicationTests {

    @Test
    fun contextLoads() {
    }

    @Test
    fun applicationContextTest() {
        main(arrayOf())
    }
}
