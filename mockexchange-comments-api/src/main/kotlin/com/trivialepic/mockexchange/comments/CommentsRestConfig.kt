package com.trivialepic.mockexchange.comments

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.stereotype.Component
import org.springframework.validation.Validator
import org.springframework.web.servlet.config.annotation.CorsRegistry
import javax.persistence.EntityManager
import javax.persistence.metamodel.EntityType
import kotlin.streams.toList


@Component
@EnableJpaAuditing
class CommentsRestConfig(private val entityManager: EntityManager) : RepositoryRestConfigurer {

    @Autowired
    lateinit var validator: Validator

    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration,
                                                      cors: CorsRegistry) {
        val domainTypes = entityManager.metamodel.entities.stream()
            .map { obj: EntityType<*> -> obj.javaType }
        config.exposeIdsFor(*domainTypes.toList().toTypedArray())
    }

    override fun configureValidatingRepositoryEventListener(v: ValidatingRepositoryEventListener) {
        v.addValidator("beforeCreate", validator)
        v.addValidator("beforeSave", validator)
    }

}
