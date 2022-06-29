package com.trivialepic.mockexchange.search.solr

import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.client.solrj.impl.CloudSolrClient
import org.apache.solr.client.solrj.impl.Http2SolrClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile


@Configuration
class SolrConfig(private val solrProperties: SolrProperties) {

    @Bean
    @ConditionalOnMissingBean(SolrClient::class)
    fun http2SolrClient(): SolrClient {
        val builder = Http2SolrClient.Builder(solrProperties.hosts.first())
        if (solrProperties.username != null) {
            builder.withBasicAuthCredentials(solrProperties.username, solrProperties.password)
        }
        return builder.build()
    }

    @Bean
    @Profile("solr-cloud")
    fun cloudSolrClient(): CloudSolrClient {
        val provider: CredentialsProvider = BasicCredentialsProvider()
        val credentials = UsernamePasswordCredentials(solrProperties.username, solrProperties.password)
        provider.setCredentials(AuthScope.ANY, credentials)
        val client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build()
        return CloudSolrClient.Builder(solrProperties.hosts)
            .withHttpClient(client)
            .build()
    }
}
