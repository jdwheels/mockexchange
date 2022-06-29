package com.trivialepic.mockexchange.populator

import com.trivialepic.mockexchange.objects.MockEntity
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@SpringBootApplication
@EntityScan(basePackageClasses = [MockEntity::class])
class PopulatorApplication

fun main(args: Array<String>) {
    runApplication<PopulatorApplication>(*args)
}
