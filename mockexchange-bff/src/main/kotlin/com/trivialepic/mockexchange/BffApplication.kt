package com.trivialepic.mockexchange

import kotlinx.coroutines.debug.CoroutinesBlockHoundIntegration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.core.ReactiveAdapterRegistry.SpringCoreBlockHoundIntegration
import org.springframework.web.server.adapter.WebHttpHandlerBuilder.SpringWebBlockHoundIntegration
import reactor.blockhound.BlockHound

@EnableCaching
@SpringBootApplication
class BffApplication

fun main(args: Array<String>) {
//    BlockHound.install(SpringCoreBlockHoundIntegration(), SpringWebBlockHoundIntegration(), CoroutinesBlockHoundIntegration())
    runApplication<BffApplication>(*args)
}
