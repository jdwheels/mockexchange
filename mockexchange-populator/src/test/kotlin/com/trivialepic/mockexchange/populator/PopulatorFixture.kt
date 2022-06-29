package com.trivialepic.mockexchange.populator

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class PopulatorFixture {
    companion object {
        @Container
        var postgres: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:14.1")
            .withDatabaseName("prop")
            .withUsername("postgres")
            .withPassword("pass")
            .withExposedPorts(5432)

        @JvmStatic
        @DynamicPropertySource
        fun registerPgProperties(registry: DynamicPropertyRegistry) {
            with(registry) {
                add("spring.datasource.url") { "${postgres.jdbcUrl}&stringtype=unspecified" }
                add("spring.datasource.password", postgres::getPassword)
                add("spring.datasource.username", postgres::getUsername)
            }
        }
    }
}
