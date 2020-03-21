package com.medium.springbatch.multijob.jobs;

import com.medium.springbatch.multijob.configuration.MultiJobAppConfigTest;
import com.medium.springbatch.multijob.components.JobLauncherComponent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {MultiJobAppConfigTest.class })
@EnableAutoConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MultiJobTests {

    private static final Logger LOG = LoggerFactory.getLogger(MultiJobTests.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobLauncherComponent launcherComponent;

    public static final String JOB_NAME = "userInnerJob";
    public static final String EXIT_CODE   = "COMPLETED";

    private List<Job> jobs;
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Before
    public void init(){
        this.jobs = launcherComponent.registerJobs();
        jobLauncherTestUtils = createJobLauncherTestUtils();
    }

    @Test
    public void givenUsuarioJob_whenJobExecuted_thenSuccess() throws Exception {
        try {
            jobLauncherTestUtils.setJob(jobs.stream().filter(j -> j.getName().equalsIgnoreCase(JOB_NAME)).findAny().orElseThrow(IllegalStateException::new));
        } catch (IllegalStateException ex) {
            LOG.error(format("Couldn't retrieve job {0} at spring's scope. ", JOB_NAME));
        }

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        assertThat(actualJobInstance.getJobName(), is(JOB_NAME));
        assertThat(actualJobExitStatus.getExitCode(), is(EXIT_CODE));
    }

    private JobLauncherTestUtils createJobLauncherTestUtils() {
        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        return jobLauncherTestUtils;
    }

}