package com.trivialepic.mockexchange.populator

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.batch.core.JobExecution
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.stream.Stream


@SpringBootTest
@SpringBatchTest
class PopulatorStepTests {

    companion object {
        @JvmStatic
        private fun testSteps() = Stream.of(
            Arguments.of("posts", 37),
            Arguments.of("postHistory", 95),
            Arguments.of("tags", 2),
            Arguments.of("votes", 789),
            Arguments.of("comments", 106),
            Arguments.of("users", 157),
            Arguments.of("badge", 7690),
        )
    }

    @Autowired
    private lateinit var jobLauncherTestUtils: JobLauncherTestUtils

//    @Autowired
//    private lateinit var jobRepositoryTestUtils: JobRepositoryTestUtils

    @ParameterizedTest
    @MethodSource
    fun testSteps(stepName: String, writeCount: Int) {
        val jobExecution: JobExecution = jobLauncherTestUtils.launchStep(stepName)
        assertThat(jobExecution.stepExecutions.size, `is`(1))
        assertThat(jobExecution.exitStatus.exitCode, `is`("COMPLETED"))
        jobExecution.stepExecutions.forEach { stepExecution -> assertThat(stepExecution.writeCount, `is`(writeCount)) }
    }


}


