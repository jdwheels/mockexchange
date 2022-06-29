package com.trivialepic.mockexchange.populator

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull

@Component
@Validated
@ConfigurationProperties(PopulatorProperties.PREFIX)
class PopulatorProperties {
    companion object {
        const val PREFIX = "mockexchange.populator"
    }
    var usersFile: Resource? = null
    var postsFile: Resource? = null
    var commentsFile: Resource? = null
    var tagsFile: Resource? = null
    var votesFile: Resource? = null
    var badgesFile: Resource? = null
    var postLinksFile: Resource? = null
    var postHistoryFile: Resource? = null
}
