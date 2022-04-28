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
    var usersFile: @NotNull Resource? = null
    var postsFile: @NotNull Resource? = null
    var commentsFile: @NotNull Resource? = null
    var tagsFile: @NotNull Resource? = null
    var votesFile: @NotNull Resource? = null
    var badgesFile: @NotNull Resource? = null
    var postLinksFile: @NotNull Resource? = null
    var postHistoryFile: @NotNull Resource? = null
}
