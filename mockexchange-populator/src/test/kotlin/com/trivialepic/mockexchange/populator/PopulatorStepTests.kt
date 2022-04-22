package com.trivialepic.mockexchange.populator

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.batch.core.JobExecution
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
@SpringBatchTest
class PopulatorStepTests {


    @Autowired
    private lateinit var jobLauncherTestUtils: JobLauncherTestUtils

//    @Autowired
//    private lateinit var jobRepositoryTestUtils: JobRepositoryTestUtils


    @ParameterizedTest
    @ValueSource(strings = ["posts", "postHistory", "postLinks", "tags", "votes", "comments", "users", "badge"])
    fun testSteps(stepName: String) {
        val jobExecution: JobExecution = jobLauncherTestUtils.launchStep(stepName)
        assertThat(jobExecution.stepExecutions.size, `is`(1))
        assertThat(jobExecution.exitStatus.exitCode, `is`("COMPLETED"))
        jobExecution.stepExecutions.forEach { stepExecution -> assertThat(stepExecution.writeCount, `is`(1)) }
    }
}
