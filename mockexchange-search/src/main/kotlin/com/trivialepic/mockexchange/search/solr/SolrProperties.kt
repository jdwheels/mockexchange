package com.trivialepic.mockexchange.search.solr

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("mockoverflow.solr")
class SolrProperties(
    val username: String,
    val password: String,
    val hosts: List<String>
)
