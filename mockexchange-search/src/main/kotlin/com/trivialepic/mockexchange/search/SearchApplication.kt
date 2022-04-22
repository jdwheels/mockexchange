package com.trivialepic.mockexchange.search

import com.trivialepic.mockexchange.objects.MockEntity
import com.trivialepic.mockexchange.search.solr.SolrProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication


@SpringBootApplication
@EnableConfigurationProperties(SolrProperties::class)
@EntityScan(basePackageClasses = [MockEntity::class])
class SearchApplication

fun main(args: Array<String>) {
    runApplication<SearchApplication>(*args)
}
