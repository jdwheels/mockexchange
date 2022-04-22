package com.trivialepic.mockexchange.populator

import com.trivialepic.mockexchange.objects.comments.MockComment
import com.trivialepic.mockexchange.objects.posts.MockPost
import com.trivialepic.mockexchange.objects.posts.MockPostHistory
import com.trivialepic.mockexchange.objects.posts.MockPostLink
import com.trivialepic.mockexchange.objects.posts.MockTag
import com.trivialepic.mockexchange.objects.posts.MockVote
import com.trivialepic.mockexchange.objects.users.MockBadge
import com.trivialepic.mockexchange.objects.users.MockUser
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import javax.persistence.EntityManagerFactory

@Configuration
@EnableBatchProcessing
class PopulatorBatchConfig(private val jobBuilderFactory: JobBuilderFactory,
                           private val entityManagerFactory: EntityManagerFactory,
                           private val stepBuilderFactory: StepBuilderFactory,
                           private val properties: PopulatorProperties) {

    @Bean
    @ConditionalOnProperty("mockoverflow.populator.posts-file")
    fun postsStep() = itemStep<MockPost>("posts", properties.postsFile)

    @Bean
    @ConditionalOnProperty("mockoverflow.populator.posts-history-file")
    fun postHistoryStep() = itemStep<MockPostHistory>("postHistory", properties.postsHistoryFile)

    @Bean
    @ConditionalOnProperty("mockoverflow.populator.post-links-file")
    fun postLinksStep() = itemStep<MockPostLink>("postLinks", properties.postLinksFile)

    @Bean
    @ConditionalOnProperty("mockoverflow.populator.tags-file")
    fun tagsStep() = itemStep<MockTag>("tags", properties.tagsFile)

    @Bean
    @ConditionalOnProperty("mockoverflow.populator.votes-file")
    fun votesStep() = itemStep<MockVote>("votes", properties.votesFile)

    @Bean
    @ConditionalOnProperty("mockoverflow.populator.users-file")
    fun usersStep() = itemStep<MockUser>("users", properties.usersFile)

    @Bean
    @ConditionalOnProperty("mockoverflow.populator.badges-file")
    fun badgesStep() = itemStep<MockBadge>("badge", properties.badgesFile)

    @Bean
    @ConditionalOnProperty("mockoverflow.populator.comments-file")
    fun commentsStep() = itemStep<MockComment>("comments", properties.commentsFile)

    private inline fun <reified T> itemStep(stepName: String, inputFile: Resource?): Step {
        return BatchStepConfig<T>(entityManagerFactory, stepBuilderFactory)
            .apply {
                name = stepName
                file = inputFile!!
                clazz = T::class.java
            }
            .step()
    }

    @Bean
    @ConditionalOnBean(Step::class)
    fun job(steps: List<Step>): Job {
        val builder = jobBuilderFactory.get("job")
            .incrementer(RunIdIncrementer())
        val first = steps.first()
        var stepBuilder = builder.start(first)
        steps.drop(1).forEach {
            stepBuilder = stepBuilder.next(it)
        }
        return stepBuilder.build()
    }
}
